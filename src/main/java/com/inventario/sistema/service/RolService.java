package com.inventario.sistema.service;

import com.inventario.sistema.entity.Rol;
import com.inventario.sistema.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    public List<Rol> getAllRoles() {
        return rolRepository.findRolesActivos();
    }

    public Rol getRolById(Long id) {
        return rolRepository.findById(id)
                .filter(Rol::getActivo)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    public Rol getRolByNombreRol(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol)
                .filter(Rol::getActivo)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));
    }

    public Rol createRol(Rol rol) {
        if (rolRepository.existsByNombreRol(rol.getNombreRol())) {
            throw new RuntimeException("El nombre del rol ya está en uso");
        }

        rol.setActivo(true);
        rol.setFechaCreacion(LocalDateTime.now());

        return rolRepository.save(rol);
    }

    public Rol updateRol(Long id, Rol rolDetails) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        if (!rolDetails.getNombreRol().equals(rol.getNombreRol()) && 
            rolRepository.existsByNombreRol(rolDetails.getNombreRol())) {
            throw new RuntimeException("El nombre del rol ya está en uso");
        }

        rol.setNombreRol(rolDetails.getNombreRol());
        rol.setDescripcion(rolDetails.getDescripcion());

        return rolRepository.save(rol);
    }

    public void deleteRol(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        rol.setActivo(false);
        rolRepository.save(rol);
    }

    public void activateRol(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));

        rol.setActivo(true);
        rolRepository.save(rol);
    }

    public boolean existsByNombreRol(String nombreRol) {
        return rolRepository.existsByNombreRol(nombreRol);
    }

    public List<Rol> getRolesActivos() {
        return rolRepository.findRolesActivos();
    }
}