package com.dannttee.mundopelotaapi.service;

import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.repository.PelotaRepository;
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

    public Optional<Pelota> obtenerPorId(Long id) {
        return pelotaRepository.findById(id);
    }

    public Pelota crear(Pelota pelota) {
        return pelotaRepository.save(pelota);
    }

    public Pelota actualizar(Long id, Pelota pelota) {
        return pelotaRepository.findById(id).map(p -> {
            p.setNombre(pelota.getNombre());
            p.setPrecio(pelota.getPrecio());
            p.setDescripcion(pelota.getDescripcion());
            p.setImageUrl(pelota.getImageUrl());
            p.setDeporte(pelota.getDeporte());
            p.setMarca(pelota.getMarca());
            p.setStock(pelota.getStock());
            return pelotaRepository.save(p);
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        if (pelotaRepository.existsById(id)) {
            pelotaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Pelota> filtrarPorDeporte(String deporte) {
        return pelotaRepository.findByDeporte(deporte);
    }
}
