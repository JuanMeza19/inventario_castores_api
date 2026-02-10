package com.inventario.sistema.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_movimiento", nullable = false)
    private TipoMovimiento tipoMovimiento;
    
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
    
    @Min(value = 0, message = "La cantidad anterior no puede ser negativa")
    @Column(name = "cantidad_anterior", nullable = false)
    private Integer cantidadAnterior;
    
    @Min(value = 0, message = "La cantidad nueva no puede ser negativa")
    @Column(name = "cantidad_nueva", nullable = false)
    private Integer cantidadNueva;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @Column(name = "fecha_movimiento")
    private LocalDateTime fechaMovimiento;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Size(max = 50, message = "La referencia no puede exceder 50 caracteres")
    @Column(name = "referencia", length = 50)
    private String referencia;
    
    @PrePersist
    protected void onCreate() {
        fechaMovimiento = LocalDateTime.now();
    }
    
    public MovimientoInventario() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Producto getProducto() {
        return producto;
    }
    
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }
    
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
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
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
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
}