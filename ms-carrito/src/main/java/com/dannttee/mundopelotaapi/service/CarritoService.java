package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.CarritoItem;
import com.dannttee.mundopelotaapi.model.Orden;
import com.dannttee.mundopelotaapi.model.OrdenDetalle;
import com.dannttee.mundopelotaapi.model.EstadoOrden;
import com.dannttee.mundopelotaapi.dto.OrdenResponse;
import com.dannttee.mundopelotaapi.repository.CarritoItemRepository;
import com.dannttee.mundopelotaapi.repository.OrdenRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Value("${api.catalogo.url}")
    private String catalogoBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    private boolean verificarStock(Long pelotaId, int cantidadSolicitada) {
        
        final String stockUrl = catalogoBaseUrl + "/api/pelotas/stock/" + pelotaId;
        
        try {
            Integer stockDisponible = restTemplate.getForObject(stockUrl, Integer.class);
            
            if (stockDisponible == null) {
                return false; 
            }
            
            return stockDisponible >= cantidadSolicitada;

        } catch (HttpClientErrorException.NotFound ex) {
            System.err.println("Error 404: Pelota con ID " + pelotaId + " no encontrada en el Catálogo.");
            return false;
        } catch (ResourceAccessException ex) {
            System.err.println("Error de conexión con el Catálogo: " + ex.getMessage());
            return false;
        } catch (Exception ex) {
            System.err.println("Error al verificar stock: " + ex.getMessage());
            return false;
        }
    }

    public List<CarritoItem> obtenerCarrito(@NonNull Long usuarioId) {
        return carritoItemRepository.findByUsuarioId(usuarioId);
    }

    public CarritoItem agregarAlCarrito(@NonNull Long usuarioId, @NonNull Long pelotaId, @NonNull Integer cantidad) {
        
        // 1. Lógica de Integración: Verificar Stock antes de continuar
        if (!verificarStock(pelotaId, cantidad)) {
            System.out.println("No hay stock suficiente o el servicio de Catálogo no está disponible para la Pelota ID: " + pelotaId);
            return null; 
        }
        
        Optional<CarritoItem> existente = carritoItemRepository.findByUsuarioIdAndPelotaId(usuarioId, pelotaId);

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;
            
            // 2. Lógica de Integración: Verificar Stock para la CANTIDAD TOTAL
            if (!verificarStock(pelotaId, nuevaCantidad)) {
                System.out.println("Aumento de cantidad excede el stock disponible.");
                return null; 
            }
            
            item.setCantidad(nuevaCantidad);
            return carritoItemRepository.save(item);
        }

        CarritoItem nuevoItem = new CarritoItem();
        nuevoItem.setUsuarioId(usuarioId);
        nuevoItem.setPelotaId(pelotaId); 
        nuevoItem.setCantidad(cantidad);

        return carritoItemRepository.save(nuevoItem);
    }

    public CarritoItem actualizarCantidad(@NonNull Long itemId, @NonNull Integer cantidad) {
        // 3. Lógica de Integración: Verificar Stock al actualizar cantidad
        return carritoItemRepository.findById(itemId).map(item -> {
            if (!verificarStock(item.getPelotaId(), cantidad)) {
                System.out.println("La nueva cantidad excede el stock disponible.");
                return null;
            }
            item.setCantidad(cantidad);
            return carritoItemRepository.save(item);
        }).orElse(null);
    }

    public boolean eliminarDelCarrito(@NonNull Long itemId) {
        if (carritoItemRepository.existsById(itemId)) {
            carritoItemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }

    @Transactional 
    public boolean vaciarCarrito(@NonNull Long usuarioId) {
        carritoItemRepository.deleteByUsuarioId(usuarioId);
        return true;
    }

    // ========== NUEVO MÉTODO PARA CREAR ORDEN ==========
    @Transactional
    public OrdenResponse crearOrden(@NonNull Long usuarioId, @NonNull List<CarritoItem> items) {
        
        // Validar que hay items
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        try {
            // Calcular total
            Double total = items.stream()
                .mapToDouble(item -> {
                    // Obtener precio unitario del catálogo
                    Double precio = obtenerPrecioPelota(item.getPelotaId());
                    return (precio != null ? precio : 0.0) * item.getCantidad();
                })
                .sum();

            // Crear la orden
            Orden orden = new Orden();
            orden.setUsuarioId(usuarioId);
            orden.setTotal(total);
            orden.setEstado(EstadoOrden.PENDIENTE);
            orden.setFechaCreacion(LocalDateTime.now());

            // Crear detalles de la orden
            List<OrdenDetalle> detalles = items.stream()
                .map(item -> {
                    OrdenDetalle detalle = new OrdenDetalle();
                    detalle.setOrden(orden);
                    detalle.setPelotaId(item.getPelotaId());
                    detalle.setCantidad(item.getCantidad());
                    
                    // Obtener precio unitario del catálogo
                    Double precio = obtenerPrecioPelota(item.getPelotaId());
                    detalle.setPrecioUnitario(precio != null ? precio : 0.0);
                    
                    return detalle;
                })
                .collect(Collectors.toList());

            orden.setDetalles(detalles);

            // Guardar en BD
            Orden ordenGuardada = ordenRepository.save(orden);

            System.out.println("Orden creada exitosamente. ID: " + ordenGuardada.getId() + " | Usuario: " + usuarioId + " | Total: " + total);

            // Retornar DTO
            return new OrdenResponse(
                ordenGuardada.getId(),
                ordenGuardada.getUsuarioId(),
                ordenGuardada.getEstado().toString(),
                ordenGuardada.getTotal(),
                ordenGuardada.getFechaCreacion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );

        } catch (Exception e) {
            System.err.println("Error al crear orden: " + e.getMessage());
            throw new RuntimeException("Error al procesar la orden: " + e.getMessage(), e);
        }
    }

    // ========== MÉTODO AUXILIAR PARA OBTENER PRECIO ==========
    private Double obtenerPrecioPelota(Long pelotaId) {
        final String precioUrl = catalogoBaseUrl + "/api/pelotas/" + pelotaId;
        
        try {
            // Asumiendo que el ms-catálogo devuelve un objeto con "precio"
            // Ajusta según tu estructura real
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> respuesta = restTemplate.getForObject(precioUrl, java.util.Map.class);
            
            if (respuesta != null && respuesta.containsKey("precio")) {
                Object precioObj = respuesta.get("precio");
                if (precioObj instanceof Number) {
                    return ((Number) precioObj).doubleValue();
                }
            }
            return null;

        } catch (HttpClientErrorException.NotFound ex) {
            System.err.println("Error 404: Pelota con ID " + pelotaId + " no encontrada.");
            return null;
        } catch (ResourceAccessException ex) {
            System.err.println("Error de conexión al obtener precio: " + ex.getMessage());
            return null;
        } catch (Exception ex) {
            System.err.println("Error al obtener precio de la pelota: " + ex.getMessage());
            return null;
        }
    }
}
