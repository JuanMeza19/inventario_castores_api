package com.inventario.sistema.controller;

import com.inventario.sistema.dto.MovimientoDTO;
import com.inventario.sistema.dto.MovimientoResponseDTO;
import com.inventario.sistema.entity.MovimientoInventario;
import com.inventario.sistema.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventario/movimientos")
@CrossOrigin(origins = "http://localhost:4200")
public class MovimientoController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/entrada")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<?> agregarInventario(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        try {
            MovimientoInventario movimiento = inventoryService.agregarInventario(
                    movimientoDTO.getProductoId(),
                    movimientoDTO.getCantidad(),
                    movimientoDTO.getUsuarioId(),
                    movimientoDTO.getObservaciones()
            );
            MovimientoResponseDTO response = MovimientoResponseDTO.fromEntity(movimiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/salida")
    @PreAuthorize("hasAnyRole('ALMACENISTA')")
    public ResponseEntity<?> restarInventario(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        try {
            MovimientoInventario movimiento = inventoryService.restarInventario(
                    movimientoDTO.getProductoId(),
                    movimientoDTO.getCantidad(),
                    movimientoDTO.getUsuarioId(),
                    movimientoDTO.getObservaciones()
            );
            MovimientoResponseDTO response = MovimientoResponseDTO.fromEntity(movimiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/producto/{productoId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<List<MovimientoResponseDTO>> getMovimientosByProducto(@PathVariable Long productoId) {
        List<MovimientoInventario> movimientos = inventoryService.getMovimientosByProducto(productoId);
        List<MovimientoResponseDTO> response = movimientos.stream()
                .map(MovimientoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}