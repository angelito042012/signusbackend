package com.example.signusbackend.entity.dto;

import java.util.List;

public class OperacionInventarioRequestDTO {
    private Integer idEncargado;
    private String tipoOperacion; // ENTRADA | SALIDA | AJUSTE
    private String motivo;

    private List<DetalleOperacionDTO> detalles;



    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public List<DetalleOperacionDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOperacionDTO> detalles) {
        this.detalles = detalles;
    }

    public Integer getIdEncargado() {
        return idEncargado;
    }

    public void setIdEncargado(Integer idEncargado) {
        this.idEncargado = idEncargado;
    }

    
}
