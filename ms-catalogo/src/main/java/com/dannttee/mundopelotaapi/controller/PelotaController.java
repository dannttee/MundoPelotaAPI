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
    public List<Pelota> obtenerTodas() {
        return pelotaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Optional<Pelota> obtenerPorId(@PathVariable @NonNull Long id) {
        return pelotaService.obtenerPorId(id);
    }

    @PostMapping
    public Pelota crear(@RequestBody @NonNull Pelota pelota) {
        return pelotaService.crear(pelota);
    }

    @PutMapping("/{id}")
    public Pelota actualizar(@PathVariable @NonNull Long id, @RequestBody @NonNull Pelota pelotaActualizada) {
        return pelotaService.actualizar(id, pelotaActualizada);
    }

    @DeleteMapping("/{id}")
    public boolean eliminar(@PathVariable @NonNull Long id) {
        return pelotaService.eliminar(id);
    }
    
    // Método de filtrado
    @GetMapping("/deporte/{deporte}")
    public List<Pelota> filtrarPorDeporte(@PathVariable @NonNull String deporte) {
        return pelotaService.filtrarPorDeporte(deporte);
    }
}