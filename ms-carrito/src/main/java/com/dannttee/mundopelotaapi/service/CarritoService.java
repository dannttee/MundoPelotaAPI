package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.CarritoItem;
import com.dannttee.mundopelotaapi.repository.CarritoItemRepository;
import lombok.NonNull; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    public List<CarritoItem> obtenerCarrito(@NonNull Long usuarioId) {
        return carritoItemRepository.findByUsuarioId(usuarioId);
    }

    public CarritoItem agregarAlCarrito(@NonNull Long usuarioId, @NonNull Long pelotaId, @NonNull Integer cantidad) {
        
        Optional<CarritoItem> existente = carritoItemRepository.findByUsuarioIdAndPelotaId(usuarioId, pelotaId);

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoItemRepository.save(item);
        }

        CarritoItem nuevoItem = new CarritoItem();
        nuevoItem.setUsuarioId(usuarioId);
        nuevoItem.setPelotaId(pelotaId); 
        nuevoItem.setCantidad(cantidad);

        return carritoItemRepository.save(nuevoItem);
    }

    public CarritoItem actualizarCantidad(@NonNull Long itemId, @NonNull Integer cantidad) {
        return carritoItemRepository.findById(itemId).map(item -> {
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