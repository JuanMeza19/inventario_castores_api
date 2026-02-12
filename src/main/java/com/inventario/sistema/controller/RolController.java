package com.inventario.sistema.controller;

import com.inventario.sistema.entity.Rol;
import com.inventario.sistema.service.RolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RolController {

    @Autowired
    private RolService rolService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<List<Rol>> getAllRoles() {
        List<Rol> roles = rolService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<Rol> getRolById(@PathVariable Long id) {
        try {
            Rol rol = rolService.getRolById(id);
            return ResponseEntity.ok(rol);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/nombre/{nombreRol}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<Rol> getRolByNombreRol(@PathVariable String nombreRol) {
        try {
            Rol rol = rolService.getRolByNombreRol(nombreRol);
            return ResponseEntity.ok(rol);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> createRol(@Valid @RequestBody Rol rol) {
        try {
            Rol newRol = rolService.createRol(rol);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRol);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> updateRol(@PathVariable Long id, @Valid @RequestBody Rol rolDetails) {
        try {
            Rol updatedRol = rolService.updateRol(id, rolDetails);
            return ResponseEntity.ok(updatedRol);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> deleteRol(@PathVariable Long id) {
        try {
            rolService.deleteRol(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rol eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> activateRol(@PathVariable Long id) {
        try {
            rolService.activateRol(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Rol activado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/exists/{nombreRol}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<Map<String, Boolean>> existsByNombreRol(@PathVariable String nombreRol) {
        boolean exists = rolService.existsByNombreRol(nombreRol);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<List<Rol>> getRolesActivos() {
        List<Rol> roles = rolService.getRolesActivos();
        return ResponseEntity.ok(roles);
    }
}