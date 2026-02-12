package com.inventario.sistema.repository;

import com.inventario.sistema.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM Usuario u JOIN FETCH u.rol WHERE u.activo = true AND u.username = :username")
    Optional<Usuario> findUsuarioActivoByUsername(@Param("username") String username);
    
    @Query("SELECT u FROM Usuario u WHERE u.activo = true ORDER BY u.nombreCompleto")
    java.util.List<Usuario> findUsuariosActivos();
    
    @Query("SELECT u FROM Usuario u WHERE u.rol.id = :idRol AND u.activo = true ORDER BY u.nombreCompleto")
    java.util.List<Usuario> findUsuariosActivosByRol(@Param("idRol") Long idRol);
}