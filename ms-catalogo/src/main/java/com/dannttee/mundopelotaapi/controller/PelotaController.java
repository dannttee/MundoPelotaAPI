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

    // CONSULTA DE STOCK (Usado por ms-carrito)
    @GetMapping("/stock/{id}")
    public ResponseEntity<Integer> getStockDisponible(@PathVariable @NonNull Long id) {
        Integer stock = pelotaService.consultarStock(id);

        if (stock == null) {
            return ResponseEntity.notFound().build(); 
        }

        return ResponseEntity.ok(stock); 
    }

    // LISTAR TODO
    @GetMapping
    public ResponseEntity<List<Pelota>> obtenerTodas() {
        return ResponseEntity.ok(pelotaService.obtenerTodas()); 
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Pelota> obtenerPorId(@PathVariable @NonNull Long id) {
        return pelotaService.obtenerPorId(id)
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    // FILTRAR POR DEPORTE
    @GetMapping("/deporte/{deporte}")
    public ResponseEntity<List<Pelota>> filtrarPorDeporte(@PathVariable @NonNull String deporte) {
        return ResponseEntity.ok(pelotaService.filtrarPorDeporte(deporte)); 
    }

    // CREAR NUEVA PELOTA (Este método soluciona el error POST Not Supported)
    @PostMapping
    public ResponseEntity<Pelota> crear(@RequestBody @NonNull Pelota pelota) {
        Pelota nuevaPelota = pelotaService.crear(pelota);
        return ResponseEntity.status(201).body(nuevaPelota); 
    }

    // ACTUALIZAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Pelota> actualizar(@PathVariable @NonNull Long id, @RequestBody @NonNull Pelota pelotaActualizada) {
        Optional<Pelota> pelotaActualizadaOpt = pelotaService.actualizar(id, pelotaActualizada);
        
        return pelotaActualizadaOpt
                .map(ResponseEntity::ok) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    // ELIMINAR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        if (pelotaService.eliminar(id)) {
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build(); 
    }
}