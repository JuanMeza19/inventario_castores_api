package com.inventario.sistema.service;

import com.inventario.sistema.dto.AuthResponseDTO;
import com.inventario.sistema.dto.LoginDTO;
import com.inventario.sistema.dto.UsuarioResponseDTO;
import com.inventario.sistema.entity.Sesion;
import com.inventario.sistema.entity.Usuario;
import com.inventario.sistema.repository.SesionRepository;
import com.inventario.sistema.repository.UsuarioRepository;
import com.inventario.sistema.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponseDTO login(LoginDTO loginDTO, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findUsuarioActivoByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(userDetails);
        
        Sesion sesion = crearSesion(usuario, request);
        
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setNombreCompleto(usuario.getNombreCompleto());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setRol(usuario.getRol().getNombreRol());
        usuarioDTO.setActivo(usuario.getActivo());
        usuarioDTO.setFechaCreacion(usuario.getFechaCreacion());
        usuarioDTO.setFechaModificacion(usuario.getFechaModificacion());
        usuarioDTO.setUltimoAcceso(usuario.getUltimoAcceso());

        return new AuthResponseDTO(token, usuarioDTO);
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtService.extractUsername(token);
            
            Usuario usuario = usuarioRepository.findUsuarioActivoByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Optional<Sesion> sesionActiva = sesionRepository.findSesionActivaByUsuario(usuario.getId());
            if (sesionActiva.isPresent()) {
                Sesion sesion = sesionActiva.get();
                sesion.setFechaFin(LocalDateTime.now());
                sesionRepository.save(sesion);
            }
        }
    }

    public boolean validateToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtService.validateToken(token);
        }
        return false;
    }

    public String refreshToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            
            // Validate the old token before issuing a new one
            if (!jwtService.validateToken(token)) {
                throw new RuntimeException("Token inválido o expirado");
            }
            
            String username = jwtService.extractUsername(token);
            
            Usuario usuario = usuarioRepository.findUsuarioActivoByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            return jwtService.generateToken(username, usuario.getRol().getNombreRol());
        }
        throw new RuntimeException("Token inválido");
    }

    private Sesion crearSesion(Usuario usuario, HttpServletRequest request) {
        Sesion sesion = new Sesion();
        sesion.setUsuario(usuario);
        sesion.setFechaInicio(LocalDateTime.now());
        sesion.setIpAddress(getClientIpAddress(request));
        sesion.setUserAgent(request.getHeader("User-Agent"));
        
        return sesionRepository.save(sesion);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}