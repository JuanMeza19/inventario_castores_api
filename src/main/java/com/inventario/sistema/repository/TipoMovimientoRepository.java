package com.inventario.sistema.repository;

import com.inventario.sistema.entity.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Long> {
    
    Optional<TipoMovimiento> findByNombreTipo(String nombreTipo);
    
    boolean existsByNombreTipo(String nombreTipo);
    
    @Query("SELECT t FROM TipoMovimiento t WHERE t.activo = true ORDER BY t.nombreTipo")
    java.util.List<TipoMovimiento> findTiposMovimientoActivos();
}