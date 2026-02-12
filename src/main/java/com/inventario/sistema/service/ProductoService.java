package com.inventario.sistema.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventario.sistema.entity.Producto;
import com.inventario.sistema.repository.ProductoRepository;
import com.inventario.sistema.dto.ProductoCreateDTO;
import com.inventario.sistema.dto.ProductoResponseDTO;
import com.inventario.sistema.entity.CategoriaProducto;
import com.inventario.sistema.entity.Usuario;
import com.inventario.sistema.repository.CategoriaProductoRepository;
import com.inventario.sistema.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    public static class ProductoConVentasDTO {
        private final Producto producto;
        private final Long totalVendido;

        public ProductoConVentasDTO(Producto producto, Long totalVendido) {
            this.producto = producto;
            this.totalVendido = totalVendido;
        }

        public Producto getProducto() {
            return producto;
        }

        public Long getTotalVendido() {
            return totalVendido;
        }
    }

    public static class ProductoConSumaDTO {
        private final Producto producto;
        private final BigDecimal sumaTotal;

        public ProductoConSumaDTO(Producto producto, BigDecimal sumaTotal) {
            this.producto = producto;
            this.sumaTotal = sumaTotal;
        }

        public Producto getProducto() {
            return producto;
        }

        public BigDecimal getSumaTotal() {
            return sumaTotal;
        }
    }
    
    @Autowired
    private ProductoRepository productRepository;
    
    @Autowired
    private CategoriaProductoRepository categoriaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Producto createProduct(Producto producto) {
        return productRepository.save(producto);
    }

    public Producto deleteProductoLogicamente(Long id) {
        Producto producto = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        producto.setActivo(false);
        return productRepository.save(producto);
    }

    public List<Producto> getProductosConVentas() {
        return productRepository.findProductosConVentas();
    }

    public List<ProductoConVentasDTO> getProductosConVentasYCantidadTotal() {
        List<Object[]> results = productRepository.findAllProductosConTotalVendido();
        List<ProductoConVentasDTO> response = new ArrayList<>();
        
        for (Object[] result : results) {
            Producto producto = (Producto) result[0];
            Long totalVendido = result[1] != null ? ((Number) result[1]).longValue() : 0L;
            
            response.add(new ProductoConVentasDTO(producto, totalVendido));
        }
        
        return response;
    }

    public List<ProductoConSumaDTO> getProductosConSumaTotalVendida() {
        List<Object[]> results = productRepository.findAllProductosConSumaTotalVendida();
        List<ProductoConSumaDTO> response = new ArrayList<>();
        
        for (Object[] result : results) {
            Producto producto = (Producto) result[0];
            BigDecimal sumaTotal = result[1] != null ? (BigDecimal) result[1] : BigDecimal.ZERO;
            
            response.add(new ProductoConSumaDTO(producto, sumaTotal));
        }
        
        return response;
    }
    
    // Métodos CRUD básicos
    public List<ProductoResponseDTO> getAllProductos() {
        return productRepository.findProductosAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public ProductoResponseDTO getProductoById(Long id) {
        Producto producto = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        if (!producto.getActivo()) {
            throw new RuntimeException("Producto está inactivo con ID: " + id);
        }
        
        return convertToResponseDTO(producto);
    }
    
    public ProductoResponseDTO createProductoFromDTO(ProductoCreateDTO productoDTO, Long usuarioId) {
        if (productRepository.existsByCodigoProducto(productoDTO.getCodigoProducto())) {
            throw new RuntimeException("Ya existe un producto con el código: " + productoDTO.getCodigoProducto());
        }
        
        CategoriaProducto categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoDTO.getIdCategoria()));
        
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));
        
        Producto producto = new Producto();
        producto.setCodigoProducto(productoDTO.getCodigoProducto());
        producto.setNombreProducto(productoDTO.getNombreProducto());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setCategoria(categoria);
        producto.setCantidadActual(productoDTO.getCantidadInicial());
        producto.setUnidadMedida(productoDTO.getUnidadMedida());
        producto.setPrecioUnitario(productoDTO.getPrecioUnitario());
        producto.setStockMinimo(productoDTO.getStockMinimo());
        producto.setStockMaximo(productoDTO.getStockMaximo());
        producto.setActivo(true);
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaModificacion(LocalDateTime.now());
        producto.setUsuarioCreacion(usuario);
        
        Producto savedProducto = productRepository.save(producto);
        return convertToResponseDTO(savedProducto);
    }
    
    public ProductoResponseDTO updateProducto(Long id, ProductoCreateDTO productoDTO, Long usuarioId) {
        Producto existingProducto = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        if (!existingProducto.getActivo()) {
            throw new RuntimeException("Producto está inactivo con ID: " + id);
        }
        
        // Verificar si el código ya existe para otro producto
        if (!existingProducto.getCodigoProducto().equals(productoDTO.getCodigoProducto()) 
                && productRepository.existsByCodigoProducto(productoDTO.getCodigoProducto())) {
            throw new RuntimeException("Ya existe otro producto con el código: " + productoDTO.getCodigoProducto());
        }
        
        CategoriaProducto categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + productoDTO.getIdCategoria()));
        
        existingProducto.setCodigoProducto(productoDTO.getCodigoProducto());
        existingProducto.setNombreProducto(productoDTO.getNombreProducto());
        existingProducto.setDescripcion(productoDTO.getDescripcion());
        existingProducto.setCategoria(categoria);
        existingProducto.setUnidadMedida(productoDTO.getUnidadMedida());
        existingProducto.setPrecioUnitario(productoDTO.getPrecioUnitario());
        existingProducto.setStockMinimo(productoDTO.getStockMinimo());
        existingProducto.setStockMaximo(productoDTO.getStockMaximo());
        existingProducto.setFechaModificacion(LocalDateTime.now());
        
        Producto updatedProducto = productRepository.save(existingProducto);
        return convertToResponseDTO(updatedProducto);
    }
    
    public ProductoResponseDTO activateProducto(Long id) {
        Producto producto = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        if (producto.getActivo()) {
            throw new RuntimeException("El producto ya está activo con ID: " + id);
        }
        
        producto.setActivo(true);
        producto.setFechaModificacion(LocalDateTime.now());
        
        Producto activatedProducto = productRepository.save(producto);
        return convertToResponseDTO(activatedProducto);
    }
    
    // Métodos de búsqueda y filtrado
    public List<ProductoResponseDTO> getProductosByCategoria(Long idCategoria) {
        return productRepository.findProductosActivosByCategoria(idCategoria).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoResponseDTO> getProductosBajoStock() {
        return productRepository.findProductosConBajoStock().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoResponseDTO> buscarProductosPorNombre(String nombre) {
        return productRepository.buscarProductosPorNombre(nombre).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoResponseDTO> buscarProductosPorCodigo(String codigo) {
        return productRepository.buscarProductosPorCodigo(codigo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    
    public boolean existsByCodigo(String codigo) {
        return productRepository.existsByCodigoProducto(codigo);
    }
    
    // Método de conversión a DTO
    private ProductoResponseDTO convertToResponseDTO(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setCodigoProducto(producto.getCodigoProducto());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria().getNombreCategoria());
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
