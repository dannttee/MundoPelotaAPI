package com.dannttee.mundopelotaapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long usuarioId;
    private Double total;
    
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenDetalle> detalles;
    
    // Constructores
    public Orden() {
    }
    
    public Orden(Long usuarioId, Double total, EstadoOrden estado) {
        this.usuarioId = usuarioId;
        this.total = total;
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
    
    public EstadoOrden getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public List<OrdenDetalle> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<OrdenDetalle> detalles) {
        this.detalles = detalles;
    }
}
