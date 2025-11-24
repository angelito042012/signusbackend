package com.example.signusbackend.entity.dto;

public class DetalleOperacionDTO {
    private Integer productoId;
    private Integer cantidad;


    
    public Integer getProductoId() {
        return productoId;
    }
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }
    public Integer getCantidad() {
        return cantidad;
    }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    
}
