package com.inventario.sistema.repository;

import com.inventario.sistema.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    
    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :idProducto ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findMovimientosByProducto(@Param("idProducto") Long idProducto);
    
    @Query("SELECT m FROM MovimientoInventario m WHERE m.usuario.id = :idUsuario ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findMovimientosByUsuario(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT m FROM MovimientoInventario m WHERE m.tipoMovimiento.id = :idTipoMovimiento ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findMovimientosByTipo(@Param("idTipoMovimiento") Long idTipoMovimiento);
    
    @Query("SELECT m FROM MovimientoInventario m WHERE m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findMovimientosPorFecha(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                       @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT m FROM MovimientoInventario m WHERE m.producto.id = :idProducto AND m.fechaMovimiento BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fechaMovimiento DESC")
    List<MovimientoInventario> findMovimientosByProductoYFecha(@Param("idProducto") Long idProducto,
                                                              @Param("fechaInicio") LocalDateTime fechaInicio,
                                                              @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(m) FROM MovimientoInventario m WHERE m.producto.id = :idProducto AND m.fechaMovimiento >= :fecha")
    Long countMovimientosRecientesByProducto(@Param("idProducto") Long idProducto, @Param("fecha") LocalDateTime fecha);
}