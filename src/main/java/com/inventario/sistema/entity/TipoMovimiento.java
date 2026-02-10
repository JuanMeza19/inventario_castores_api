package com.inventario.sistema.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tipos_movimiento")
public class TipoMovimiento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_movimiento")
    private Long id;
    
    @NotBlank(message = "El nombre del tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El nombre del tipo de movimiento no puede exceder 20 caracteres")
    @Column(name = "nombre_tipo", unique = true, nullable = false, length = 20)
    private String nombreTipo;
    
    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    @Column(name = "descripcion", length = 100)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "afecta_inventario", nullable = false)
    private AfectaInventario afectaInventario;
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @OneToMany(mappedBy = "tipoMovimiento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientoInventario> movimientos;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
    
    public TipoMovimiento() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombreTipo() {
        return nombreTipo;
    }
    
    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public AfectaInventario getAfectaInventario() {
        return afectaInventario;
    }
    
    public void setAfectaInventario(AfectaInventario afectaInventario) {
        this.afectaInventario = afectaInventario;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<MovimientoInventario> getMovimientos() {
        return movimientos;
    }
    
    public void setMovimientos(List<MovimientoInventario> movimientos) {
        this.movimientos = movimientos;
    }
    
    public enum AfectaInventario {
        SUMA,
        RESTA
    }
}