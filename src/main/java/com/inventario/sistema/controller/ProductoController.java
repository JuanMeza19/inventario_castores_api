package com.inventario.sistema.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventario.sistema.dto.ProductoCreateDTO;
import com.inventario.sistema.dto.ProductoResponseDTO;
import com.inventario.sistema.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
        try {
            List<ProductoResponseDTO> productos = productoService.getAllProductos();
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // GET /api/productos/{id} - Obtener producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<?> getProductoById(@PathVariable Long id) {
        try {
            ProductoResponseDTO producto = productoService.getProductoById(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // POST /api/productos - Crear nuevo producto
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<?> createProducto(@Valid @RequestBody ProductoCreateDTO productoDTO) {
        try {
            Long usuarioId = 1L;
            ProductoResponseDTO nuevoProducto = productoService.createProductoFromDTO(productoDTO, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MANAGER')")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoCreateDTO productoDTO) {
        try {
            Long usuarioId = 1L;
            ProductoResponseDTO productoActualizado = productoService.updateProducto(id, productoDTO, usuarioId);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProductoLogicamente(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Producto eliminado correctamente con ID: " + id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> activateProducto(@PathVariable Long id) {
        try {
            ProductoResponseDTO producto = productoService.activateProducto(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/categoria/{idCategoria}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<?> getProductosByCategoria(@PathVariable Long idCategoria) {
        try {
            List<ProductoResponseDTO> productos = productoService.getProductosByCategoria(idCategoria);
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/bajo-stock")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<?> getProductosBajoStock() {
        try {
            List<ProductoResponseDTO> productos = productoService.getProductosBajoStock();
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<?> buscarProductos(@RequestParam(required = false) String nombre,
                                           @RequestParam(required = false) String codigo) {
        try {
            if (nombre != null && !nombre.trim().isEmpty()) {
                List<ProductoResponseDTO> productos = productoService.buscarProductosPorNombre(nombre);
                return ResponseEntity.ok(productos);
            } else if (codigo != null && !codigo.trim().isEmpty()) {
                List<ProductoResponseDTO> productos = productoService.buscarProductosPorCodigo(codigo);
                return ResponseEntity.ok(productos);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Debe proporcionar un nombre o código para buscar");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/existentes/codigo/{codigo}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<?> checkExistsByCodigo(@PathVariable String codigo) {
        try {
            boolean exists = productoService.existsByCodigo(codigo);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("codigo", codigo);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}