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
    
   @Query("SELECT p FROM Producto p")
    List<Producto> findProductosAll();

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
    
    @Query("SELECT DISTINCT p FROM Producto p INNER JOIN MovimientoInventario m ON p.id = m.producto.id WHERE p.activo = true ORDER BY p.nombreProducto")
    List<Producto> findProductosConVentas();
    
    @Query("SELECT p FROM Producto p INNER JOIN MovimientoInventario m ON p.id = m.producto.id WHERE m.tipoMovimiento.afectaInventario = 'RESTA' AND p.activo = true GROUP BY p.id ORDER BY p.nombreProducto")
    List<Producto> findProductosConVentasContadas();
    
    @Query("SELECT p, SUM(m.cantidad) as totalVendido FROM Producto p LEFT JOIN MovimientoInventario m ON p.id = m.producto.id WHERE m.tipoMovimiento.afectaInventario = 'RESTA' GROUP BY p.id ORDER BY p.nombreProducto")
    List<Object[]> findAllProductosConTotalVendido();
    
    @Query("SELECT p, COALESCE(SUM(m.cantidad * p.precioUnitario), 0) as totalVenta FROM Producto p LEFT JOIN MovimientoInventario m ON p.id = m.producto.id WHERE m.tipoMovimiento.afectaInventario = 'RESTA' OR m.tipoMovimiento.afectaInventario IS NULL GROUP BY p.id ORDER BY p.nombreProducto")
    List<Object[]> findAllProductosConSumaTotalVendida();
    
    @Query("SELECT p FROM Producto p WHERE p.activo = false ORDER BY p.nombreProducto")
    List<Producto> findProductosInactivos();
}