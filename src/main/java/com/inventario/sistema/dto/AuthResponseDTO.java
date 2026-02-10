package com.inventario.sistema.dto;

public class AuthResponseDTO {
    
    private String token;
    private String type = "Bearer";
    private UsuarioResponseDTO usuario;
    
    public AuthResponseDTO() {}
    
    public AuthResponseDTO(String token, UsuarioResponseDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public UsuarioResponseDTO getUsuario() {
        return usuario;
    }
    
    public void setUsuario(UsuarioResponseDTO usuario) {
        this.usuario = usuario;
    }
}