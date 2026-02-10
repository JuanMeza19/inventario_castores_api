package com.inventario.sistema.repository;

import com.inventario.sistema.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Long> {
    
    @Query("SELECT s FROM Sesion s WHERE s.usuario.id = :idUsuario AND s.fechaFin IS NULL ORDER BY s.fechaInicio DESC")
    Optional<Sesion> findSesionActivaByUsuario(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT s FROM Sesion s WHERE s.usuario.id = :idUsuario ORDER BY s.fechaInicio DESC")
    List<Sesion> findSesionesByUsuario(@Param("idUsuario") Long idUsuario);
    
    @Query("SELECT s FROM Sesion s WHERE s.fechaInicio BETWEEN :fechaInicio AND :fechaFin ORDER BY s.fechaInicio DESC")
    List<Sesion> findSesionesPorFecha(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                      @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(s) FROM Sesion s WHERE s.usuario.id = :idUsuario AND s.fechaInicio >= :fecha")
    Long countSesionesRecientesByUsuario(@Param("idUsuario") Long idUsuario, @Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT s FROM Sesion s WHERE s.fechaFin IS NULL")
    List<Sesion> findSesionesActivas();
}