package com.inventario.sistema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public class MovimientoCreateDTO {
    
    @NotBlank(message = "El código del producto es obligatorio")
    private String codigoProducto;
    
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipoMovimiento;
    
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
    
    private String observaciones;
    
    private String referencia;
    
    public MovimientoCreateDTO() {}
    
    public String getCodigoProducto() {
        return codigoProducto;
    }
    
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
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
}