package com.inventario.sistema.repository;

import com.inventario.sistema.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByCodigoProducto(String codigoProducto);
    
    boolean existsByCodigoProducto(String codigoProducto);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.nombreProducto")
    List<Producto> findProductosActivos();
    
    @Query("SELECT p FROM Producto p WHERE p.categoria.id = :idCategoria AND p.activo = true ORDER BY p.nombreProducto")
    List<Producto> findProductosActivosByCategoria(@Param("idCategoria") Long idCategoria);
    
    @Query("SELECT p FROM Producto p WHERE p.cantidadActual <= p.stockMinimo AND p.activo = true ORDER BY p.nombreProducto")
    List<Producto> findProductosConBajoStock();
    
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%')) AND p.activo = true ORDER BY p.nombreProducto")
    List<Producto> buscarProductosPorNombre(@Param("nombre") String nombre);
    
    @Query("SELECT p FROM Producto p WHERE LOWER(p.codigoProducto) LIKE LOWER(CONCAT('%', :codigo, '%')) AND p.activo = true ORDER BY p.codigoProducto")
    List<Producto> buscarProductosPorCodigo(@Param("codigo") String codigo);
}