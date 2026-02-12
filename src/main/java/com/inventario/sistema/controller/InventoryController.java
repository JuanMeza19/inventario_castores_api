package com.inventario.sistema.controller;

import com.inventario.sistema.dto.ProductoResponseDTO;
import com.inventario.sistema.entity.Producto;
import com.inventario.sistema.service.InventoryService;
import com.inventario.sistema.service.ProductoService;
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
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:4200")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductoService productoService;

    @GetMapping("/productos/activos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosActivos() {
        List<Producto> productos = inventoryService.getProductosActivos();
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/productos/inactivos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<List<ProductoResponseDTO>> getProductosInactivos() {
        List<Producto> productos = inventoryService.getProductosInactivos();
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/productos/todos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ALMACENISTA')")
    public ResponseEntity<List<ProductoResponseDTO>> getAllProductos() {
        List<Producto> productos = inventoryService.getAllProductos();
        List<ProductoResponseDTO> dtos = productos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/productos/{id}/dar-de-baja")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> darDeBajaProducto(@PathVariable Long id) {
        try {
            productoService.deleteProductoLogicamente(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Producto dado de baja exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/productos/{id}/activar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<Map<String, String>> activarProducto(@PathVariable Long id) {
        try {
            productoService.activateProducto(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Producto activado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    private ProductoResponseDTO convertToDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setCodigoProducto(producto.getCodigoProducto());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria() != null ? producto.getCategoria().getNombreCategoria() : null);
        dto.setCantidadActual(producto.getCantidadActual());
        dto.setUnidadMedida(producto.getUnidadMedida());
        dto.setPrecioUnitario(producto.getPrecioUnitario());
        dto.setStockMinimo(producto.getStockMinimo());
        dto.setStockMaximo(producto.getStockMaximo());
        dto.setActivo(producto.getActivo());
        dto.setUsuarioCreacion(producto.getUsuarioCreacion() != null ? 
                producto.getUsuarioCreacion().getNombreCompleto() : null);
        dto.setBajoStock(producto.getCantidadActual() <= producto.getStockMinimo());
        return dto;
    }
}