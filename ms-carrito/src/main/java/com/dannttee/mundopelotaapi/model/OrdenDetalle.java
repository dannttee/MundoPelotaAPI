package com.dannttee.mundopelotaapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "orden_detalles")
public class OrdenDetalle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;
    
    private Long pelotaId;
    private Integer cantidad;
    private Double precioUnitario;
    
    // Constructores
    public OrdenDetalle() {
    }
    
    public OrdenDetalle(Orden orden, Long pelotaId, Integer cantidad, Double precioUnitario) {
        this.orden = orden;
        this.pelotaId = pelotaId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Orden getOrden() {
        return orden;
    }
    
    public void setOrden(Orden orden) {
        this.orden = orden;
    }
    
    public Long getPelotaId() {
        return pelotaId;
    }
    
    public void setPelotaId(Long pelotaId) {
        this.pelotaId = pelotaId;
    }
    
    public Integer getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    
    public Double getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
