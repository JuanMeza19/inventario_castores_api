package com.inventario.sistema.repository;

import com.inventario.sistema.entity.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {
    
    Optional<CategoriaProducto> findByNombreCategoria(String nombreCategoria);
    
    boolean existsByNombreCategoria(String nombreCategoria);
    
    @Query("SELECT c FROM CategoriaProducto c WHERE c.activo = true ORDER BY c.nombreCategoria")
    java.util.List<CategoriaProducto> findCategoriasActivas();
}