package com.dannttee.mundopelotaapi.dto;

public class OrdenResponse {
    private Long id;
    private Long usuarioId;
    private String estado;
    private Double total;
    private String fechaCreacion;
    
    public OrdenResponse(Long id, Long usuarioId, String estado, Double total, String fechaCreacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.estado = estado;
        this.total = total;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Getters
    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getEstado() { return estado; }
    public Double getTotal() { return total; }
    public String getFechaCreacion() { return fechaCreacion; }
}