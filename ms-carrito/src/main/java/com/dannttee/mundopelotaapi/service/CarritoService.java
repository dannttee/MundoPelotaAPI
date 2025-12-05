package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.CarritoItem;
import com.dannttee.mundopelotaapi.repository.CarritoItemRepository;
import lombok.NonNull; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate; 
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

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
}