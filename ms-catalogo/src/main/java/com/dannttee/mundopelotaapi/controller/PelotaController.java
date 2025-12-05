package com.dannttee.mundopelotaapi.controller;

import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.service.PelotaService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; 

@RestController
@RequestMapping("/api/pelotas")
public class PelotaController {

    @Autowired
    private PelotaService pelotaService;

    @GetMapping("/stock/{id}")
    public ResponseEntity<Integer> getStockDisponible(@PathVariable @NonNull Long id) {
        Integer stock = pelotaService.consultarStock(id);

        if (stock == null) {
            return ResponseEntity.notFound().build(); 
        }

        return ResponseEntity.ok(stock); 
    }

    @GetMapping
    public ResponseEntity<List<Pelota>> obtenerTodas() {
        return ResponseEntity.ok(pelotaService.obtenerTodas()); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelota> obtenerPorId(@PathVariable @NonNull Long id) {
        return pelotaService.obtenerPorId(id)
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    @GetMapping("/deporte/{deporte}")
    public ResponseEntity<List<Pelota>> filtrarPorDeporte(@PathVariable @NonNull String deporte) {
        return ResponseEntity.ok(pelotaService.filtrarPorDeporte(deporte)); 
    }

    @PostMapping
    public ResponseEntity<Pelota> crear(@RequestBody @NonNull Pelota pelota) {
        Pelota nuevaPelota = pelotaService.crear(pelota);
        return ResponseEntity.status(201).body(nuevaPelota); 
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pelota> actualizar(@PathVariable @NonNull Long id, @RequestBody @NonNull Pelota pelotaActualizada) {
        Optional<Pelota> pelotaActualizadaOpt = pelotaService.actualizar(id, pelotaActualizada);
        
        return pelotaActualizadaOpt
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        if (pelotaService.eliminar(id)) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }
}