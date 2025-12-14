package com.dannttee.mundopelotaapi.controller;

import com.dannttee.mundopelotaapi.model.ApiResponse;
import com.dannttee.mundopelotaapi.model.Orden;
import com.dannttee.mundopelotaapi.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrdenController {
    
    @Autowired
    private OrdenRepository ordenRepository;
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<Orden>>> obtenerOrdenesPorUsuario(
            @PathVariable Long usuarioId) {
        List<Orden> ordenes = ordenRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Órdenes obtenidas", ordenes));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Orden>> obtenerOrden(@PathVariable Long id) {
        Orden orden = ordenRepository.findById(id).orElse(null);
        if (orden == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("Orden no encontrada"));
        }
        return ResponseEntity.ok(ApiResponse.success("Orden obtenida", orden));
    }
}
