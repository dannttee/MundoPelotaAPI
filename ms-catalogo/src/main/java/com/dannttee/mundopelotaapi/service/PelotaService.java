package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.repository.PelotaRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PelotaService {

    @Autowired
    private PelotaRepository pelotaRepository;

    public List<Pelota> obtenerTodas() {
        return pelotaRepository.findAll();
    }

    public Optional<Pelota> obtenerPorId(@NonNull Long id) {
        return pelotaRepository.findById(id);
    }

    // Renombrado a 'crear' para que coincida con tu Controller
    public Pelota crear(@NonNull Pelota pelota) {
        return pelotaRepository.save(pelota);
    }

    // Método nuevo para 'actualizar'
    public Pelota actualizar(@NonNull Long id, @NonNull Pelota pelotaActualizada) {
        return pelotaRepository.findById(id).map(pelotaExistente -> {
            pelotaExistente.setNombre(pelotaActualizada.getNombre());
            pelotaExistente.setMarca(pelotaActualizada.getMarca());
            pelotaExistente.setPrecio(pelotaActualizada.getPrecio());
            pelotaExistente.setDeporte(pelotaActualizada.getDeporte());
            pelotaExistente.setStock(pelotaActualizada.getStock());
            pelotaExistente.setDescripcion(pelotaActualizada.getDescripcion());
            pelotaExistente.setImageUrl(pelotaActualizada.getImageUrl());
            return pelotaRepository.save(pelotaExistente);
        }).orElse(null);
    }

    // Renombrado a 'eliminar'
    public boolean eliminar(@NonNull Long id) {
        if (pelotaRepository.existsById(id)) {
            pelotaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método nuevo para filtrar
    public List<Pelota> filtrarPorDeporte(@NonNull String deporte) {
        return pelotaRepository.findByDeporte(deporte);
    }
}