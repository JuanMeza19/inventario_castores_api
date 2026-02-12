package com.inventario.sistema.dto;

import java.time.LocalDateTime;

public class MovimientoResponseDTO {
    
    private Long id;
    private Long productoId;
    private String codigoProducto;
    private String nombreProducto;
    private String tipoMovimiento;
    private Integer cantidad;
    private Integer cantidadAnterior;
    private Integer cantidadNueva;
    private String usuario;
    private LocalDateTime fechaMovimiento;
    private String observaciones;
    private String referencia;
    
    public MovimientoResponseDTO() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProductoId() {
        return productoId;
    }
    
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }
    
    public String getNombreProducto() {
        return nombreProducto;
    }
    
    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }
    
    public String getTipoMovimiento() {
        return tipoMovimiento;
    }
    
    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public Integer getCantidadAnterior() {
        return cantidadAnterior;
    }
    
    public void setCantidadAnterior(Integer cantidadAnterior) {
        this.cantidadAnterior = cantidadAnterior;
    }
    
    public Integer getCantidadNueva() {
        return cantidadNueva;
    }
    
    public void setCantidadNueva(Integer cantidadNueva) {
        this.cantidadNueva = cantidadNueva;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }
    
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String getReferencia() {
        return referencia;
    }
    
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    
    public static MovimientoResponseDTO fromEntity(com.inventario.sistema.entity.MovimientoInventario movimiento) {
        MovimientoResponseDTO dto = new MovimientoResponseDTO();
        dto.setId(movimiento.getId());
        dto.setProductoId(movimiento.getProducto().getId());
        dto.setCodigoProducto(movimiento.getProducto().getCodigoProducto());
        dto.setNombreProducto(movimiento.getProducto().getNombreProducto());
        dto.setTipoMovimiento(movimiento.getTipoMovimiento().getNombreTipo());
        dto.setCantidad(movimiento.getCantidad());
        dto.setCantidadAnterior(movimiento.getCantidadAnterior());
        dto.setCantidadNueva(movimiento.getCantidadNueva());
        dto.setUsuario(movimiento.getUsuario().getNombreCompleto());
        dto.setFechaMovimiento(movimiento.getFechaMovimiento());
        dto.setObservaciones(movimiento.getObservaciones());
        dto.setReferencia(movimiento.getReferencia());
        return dto;
    }
}