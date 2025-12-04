package com.dannttee.mundopelotaapi.controller;

import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.service.PelotaService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController 
@RequestMapping("/api/pelotas") 
public class PelotaController {

    @Autowired
    private PelotaService pelotaService;

    @GetMapping
    public List<Pelota> obtenerTodas() {
        return pelotaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Pelota> obtenerPorId(@PathVariable Long id) {
        return pelotaService.obtenerPorId(id);
    }

    @PostMapping
    public Pelota crear(@RequestBody Pelota pelota) {
        return pelotaService.crear(pelota);
    }

    @PutMapping("/{id}")
    public Pelota actualizar(@PathVariable Long id, @RequestBody Pelota pelota) {
        return pelotaService.actualizar(id, pelota);
    }

    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable Long id) {
        return pelotaService.eliminar(id);
    }
}