package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.CarritoItem;
import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.repository.CarritoItemRepository;
import com.dannttee.mundopelotaapi.repository.PelotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private PelotaRepository pelotaRepository;

    public List<CarritoItem> obtenerCarrito(Long usuarioId) {
        return carritoItemRepository.findByUsuarioId(usuarioId);
    }

    public CarritoItem agregarAlCarrito(Long usuarioId, Long pelotaId, Integer cantidad) {
        Optional<Pelota> pelota = pelotaRepository.findById(pelotaId);
        if (pelota.isEmpty()) {
            throw new RuntimeException("Pelota no encontrada");
        }

        Optional<CarritoItem> existente = carritoItemRepository.findByUsuarioIdAndPelotaId(usuarioId, pelotaId);

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoItemRepository.save(item);
        }

        CarritoItem nuevoItem = new CarritoItem();
        nuevoItem.setUsuarioId(usuarioId);
        nuevoItem.setPelota(pelota.get());
        nuevoItem.setCantidad(cantidad);

        return carritoItemRepository.save(nuevoItem);
    }

    public CarritoItem actualizarCantidad(Long itemId, Integer cantidad) {
        return carritoItemRepository.findById(itemId).map(item -> {
            item.setCantidad(cantidad);
            return carritoItemRepository.save(item);
        }).orElse(null);
    }

    public boolean eliminarDelCarrito(Long itemId) {
        if (carritoItemRepository.existsById(itemId)) {
            carritoItemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }

    public boolean vaciarCarrito(Long usuarioId) {
        carritoItemRepository.deleteByUsuarioId(usuarioId);
        return true;
    }
}
