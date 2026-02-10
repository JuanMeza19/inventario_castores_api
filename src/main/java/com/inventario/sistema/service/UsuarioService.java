package com.inventario.sistema.service;

import com.inventario.sistema.dto.UsuarioCreateDTO;
import com.inventario.sistema.dto.UsuarioResponseDTO;
import com.inventario.sistema.entity.Usuario;
import com.inventario.sistema.repository.UsuarioRepository;
import com.inventario.sistema.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDTO> getAllUsuarios() {
        return usuarioRepository.findUsuariosActivos().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario no está activo");
        }
        
        return convertToDTO(usuario);
    }

    public UsuarioResponseDTO getUsuarioByUsername(String username) {
        Usuario usuario = usuarioRepository.findUsuarioActivoByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        
        return convertToDTO(usuario);
    }

    public UsuarioResponseDTO createUsuario(UsuarioCreateDTO usuarioCreateDTO) {
        if (usuarioRepository.existsByUsername(usuarioCreateDTO.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        
        if (usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioCreateDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioCreateDTO.getPassword()));
        usuario.setNombreCompleto(usuarioCreateDTO.getNombreCompleto());
        usuario.setEmail(usuarioCreateDTO.getEmail());
        usuario.setActivo(true);
        
        rolRepository.findByNombreRol(usuarioCreateDTO.getNombreRol())
                .ifPresentOrElse(
                    rol -> {
                        if (!rol.getActivo()) {
                            throw new RuntimeException("Rol no está activo: " + usuarioCreateDTO.getNombreRol());
                        }
                        usuario.setRol(rol);
                    },
                    () -> { throw new RuntimeException("Rol no encontrado con nombre: " + usuarioCreateDTO.getNombreRol()); }
                );

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    public UsuarioResponseDTO updateUsuario(Long id, UsuarioCreateDTO usuarioCreateDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (!usuario.getUsername().equals(usuarioCreateDTO.getUsername()) && 
            usuarioRepository.existsByUsername(usuarioCreateDTO.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }

        if (!usuario.getEmail().equals(usuarioCreateDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioCreateDTO.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        usuario.setUsername(usuarioCreateDTO.getUsername());
        usuario.setNombreCompleto(usuarioCreateDTO.getNombreCompleto());
        usuario.setEmail(usuarioCreateDTO.getEmail());
        
        if (usuarioCreateDTO.getPassword() != null && !usuarioCreateDTO.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioCreateDTO.getPassword()));
        }
        
        if (!usuarioCreateDTO.getNombreRol().equals(usuario.getRol().getNombreRol())) {
            rolRepository.findByNombreRol(usuarioCreateDTO.getNombreRol())
                    .ifPresentOrElse(
                        rol -> {
                            if (!rol.getActivo()) {
                                throw new RuntimeException("Rol no está activo: " + usuarioCreateDTO.getNombreRol());
                            }
                            usuario.setRol(rol);
                        },
                        () -> { throw new RuntimeException("Rol no encontrado con nombre: " + usuarioCreateDTO.getNombreRol()); }
                    );
        }

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return convertToDTO(savedUsuario);
    }

    public void deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public void activateUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }

    public List<UsuarioResponseDTO> getUsuariosByRol(String nombreRol) {
        return usuarioRepository.findUsuariosActivos().stream()
                .filter(usuario -> usuario.getRol().getNombreRol().equals(nombreRol))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    private UsuarioResponseDTO convertToDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol().getNombreRol());
        dto.setActivo(usuario.getActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        dto.setFechaModificacion(usuario.getFechaModificacion());
        dto.setUltimoAcceso(usuario.getUltimoAcceso());
        return dto;
    }
}