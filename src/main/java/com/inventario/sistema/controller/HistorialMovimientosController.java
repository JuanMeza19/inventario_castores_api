package com.inventario.sistema.controller;

import com.inventario.sistema.dto.MovimientoResponseDTO;
import com.inventario.sistema.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventario/historial")
@CrossOrigin(origins = "http://localhost:4200")
public class HistorialMovimientosController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<List<MovimientoResponseDTO>> getAllMovimientos() {
        List<MovimientoResponseDTO> movimientos = inventoryService.getAllMovimientos().stream()
                .map(MovimientoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/tipo/{tipoMovimiento}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<List<MovimientoResponseDTO>> getMovimientosByTipo(@PathVariable String tipoMovimiento) {
        try {
            List<MovimientoResponseDTO> movimientos = inventoryService.getMovimientosByTipo(tipoMovimiento.toUpperCase())
                    .stream()
                    .map(MovimientoResponseDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(movimientos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/entradas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<List<MovimientoResponseDTO>> getEntradas() {
        List<MovimientoResponseDTO> movimientos = inventoryService.getMovimientosByTipo("ENTRADA")
                .stream()
                .map(MovimientoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/salidas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<List<MovimientoResponseDTO>> getSalidas() {
        List<MovimientoResponseDTO> movimientos = inventoryService.getMovimientosByTipo("SALIDA")
                .stream()
                .map(MovimientoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movimientos);
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<List<MovimientoResponseDTO>> getMovimientosPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        
        List<MovimientoResponseDTO> movimientos = inventoryService.getMovimientosPorFecha(fechaInicio, fechaFin)
                .stream()
                .map(MovimientoResponseDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movimientos);
    }
}