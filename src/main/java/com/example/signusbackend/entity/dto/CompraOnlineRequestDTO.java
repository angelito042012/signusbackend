package com.example.signusbackend.entity.dto;

import java.util.List;

public class CompraOnlineRequestDTO {
    private Integer idCliente;
    private String metodoPago;
    private String direccionEnvio;
    private String tipoEnvio; // Ejemplo: "ESTÁNDAR", "EXPRESS"
    private List<ProductoCompraDTO> productos;



    public Integer getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }
    public String getDireccionEnvio() {
        return direccionEnvio;
    }
    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }
    public String getTipoEnvio() {
        return tipoEnvio;
    }
    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }
    public List<ProductoCompraDTO> getProductos() {
        return productos;
    }
    public void setProductos(List<ProductoCompraDTO> productos) {
        this.productos = productos;
    }
    public String getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}
