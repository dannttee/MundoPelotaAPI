package com.dannttee.mundopelotaapi.controller;

import com.dannttee.mundopelotaapi.model.ApiResponse;
import com.dannttee.mundopelotaapi.model.Pelota;
import com.dannttee.mundopelotaapi.service.PelotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pelotas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PelotaController {

    @Autowired
    private PelotaService pelotaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Pelota>>> obtenerTodas() {
        List<Pelota> pelotas = pelotaService.obtenerTodas();
        return ResponseEntity.ok(ApiResponse.success("Pelotas obtenidas correctamente", pelotas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Pelota>> obtenerPorId(@PathVariable Long id) {
        return pelotaService.obtenerPorId(id)
                .map(pelota -> ResponseEntity.ok(ApiResponse.success("Pelota encontrada", pelota)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Pelota no encontrada")));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Pelota>> crear(@RequestBody Pelota pelota) {
        Pelota nueva = pelotaService.crear(pelota);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pelota creada correctamente", nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Pelota>> actualizar(@PathVariable Long id, @RequestBody Pelota pelota) {
        Pelota actualizada = pelotaService.actualizar(id, pelota);
        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Pelota no encontrada"));
        }
        return ResponseEntity.ok(ApiResponse.success("Pelota actualizada correctamente", actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        if (pelotaService.eliminar(id)) {
            return ResponseEntity.ok(ApiResponse.success("Pelota eliminada correctamente", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Pelota no encontrada"));
    }

    @GetMapping("/deporte/{deporte}")
    public ResponseEntity<ApiResponse<List<Pelota>>> filtrarPorDeporte(@PathVariable String deporte) {
        List<Pelota> pelotas = pelotaService.filtrarPorDeporte(deporte);
        return ResponseEntity.ok(ApiResponse.success("Pelotas filtradas", pelotas));
    }
}
