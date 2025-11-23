package com.example.signusbackend.entity.dto;

public class DetalleVentaDTO {
    private Integer idProducto;
    private Integer cantidad;
    private Double precioUnitario;


    
    public Integer getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
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
