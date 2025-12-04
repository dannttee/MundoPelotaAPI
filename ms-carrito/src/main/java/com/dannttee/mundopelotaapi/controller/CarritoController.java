package com.dannttee.mundopelotaapi.controller;

import com.dannttee.mundopelotaapi.model.ApiResponse;
import com.dannttee.mundopelotaapi.model.CarritoItem;
import com.dannttee.mundopelotaapi.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CarritoItem>>> obtenerCarrito(@RequestParam Long usuarioId) {
        List<CarritoItem> items = carritoService.obtenerCarrito(usuarioId);
        return ResponseEntity.ok(ApiResponse.success("Carrito obtenido", items));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CarritoItem>> agregarAlCarrito(@RequestBody Map<String, Object> body) {
        Long usuarioId = ((Number) body.get("usuarioId")).longValue();
        Long pelotaId = ((Number) body.get("pelotaId")).longValue();
        Integer cantidad = ((Number) body.get("cantidad")).intValue();

        CarritoItem item = carritoService.agregarAlCarrito(usuarioId, pelotaId, cantidad);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item agregado al carrito", item));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<CarritoItem>> actualizarCantidad(
            @PathVariable Long itemId,
            @RequestBody Map<String, Integer> body) {
        Integer cantidad = body.get("cantidad");
        CarritoItem actualizado = carritoService.actualizarCantidad(itemId, cantidad);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Item no encontrado"));
        }
        return ResponseEntity.ok(ApiResponse.success("Cantidad actualizada", actualizado));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<String>> eliminarDelCarrito(@PathVariable Long itemId) {
        if (carritoService.eliminarDelCarrito(itemId)) {
            return ResponseEntity.ok(ApiResponse.success("Item eliminado del carrito", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("Item no encontrado"));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> vaciarCarrito(@RequestParam Long usuarioId) {
        if (carritoService.vaciarCarrito(usuarioId)) {
            return ResponseEntity.ok(ApiResponse.success("Carrito vaciado", ""));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Error al vaciar carrito"));
    }
}
