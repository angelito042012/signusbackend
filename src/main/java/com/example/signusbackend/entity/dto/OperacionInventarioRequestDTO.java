package com.example.signusbackend.entity.dto;

import java.util.List;

public class OperacionInventarioRequestDTO {
    private Integer encargadoId;
    private String tipoOperacion; // ENTRADA | SALIDA | AJUSTE
    private String motivo;

    private List<DetalleOperacionDTO> detalles;



    public Integer getEncargadoId() {
        return encargadoId;
    }

    public void setEncargadoId(Integer encargadoId) {
        this.encargadoId = encargadoId;
    }

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

    
}
