package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.DetalleVenta;

public interface DetalleVentaService {

    List<DetalleVenta> listarPorVenta(Integer idVenta);

    DetalleVenta agregarDetalle(DetalleVenta detalle);

    void eliminarDetalle(Integer idDetalle);
}
