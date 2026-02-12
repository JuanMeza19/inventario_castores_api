package com.inventario.sistema.controller;

import com.inventario.sistema.dto.UsuarioCreateDTO;
import com.inventario.sistema.dto.UsuarioResponseDTO;
import com.inventario.sistema.service.UsuarioService;
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
@RequestMapping("/api/public/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<List<UsuarioResponseDTO>> getAllUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER') or #id == authentication.principal.id")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioById(@PathVariable Long id) {
        try {
            UsuarioResponseDTO usuario = usuarioService.getUsuarioById(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER') or #username == authentication.principal.username")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioByUsername(@PathVariable String username) {
        try {
            UsuarioResponseDTO usuario = usuarioService.getUsuarioByUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<?> createUsuario(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        try {
            UsuarioResponseDTO usuario = usuarioService.createUsuario(usuarioCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER') or #id == authentication.principal.id")
    public ResponseEntity<?> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) {
        try {
            UsuarioResponseDTO usuario = usuarioService.updateUsuario(id, usuarioCreateDTO);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<Map<String, String>> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteUsuario(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<Map<String, String>> activateUsuario(@PathVariable Long id) {
        try {
            usuarioService.activateUsuario(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario activado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/rol/{nombreRol}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<List<UsuarioResponseDTO>> getUsuariosByRol(@PathVariable String nombreRol) {
        List<UsuarioResponseDTO> usuarios = usuarioService.getUsuariosByRol(nombreRol);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/exists/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Map<String, Boolean>> existsByUsername(@PathVariable String username) {
        boolean exists = usuarioService.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/email/{email}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<Map<String, Boolean>> existsByEmail(@PathVariable String email) {
        boolean exists = usuarioService.existsByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        String username = extractUsernameFromToken(token);
        try {
            UsuarioResponseDTO usuario = usuarioService.getUsuarioByUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private String extractUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new RuntimeException("Token inválido");
    }
}