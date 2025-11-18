package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.MetodoPago;

public interface MetodoPagoService {
    List<MetodoPago> listarMetodos();

    MetodoPago obtenerPorId(Integer idMetodo);

    MetodoPago crearMetodo(MetodoPago metodoPago);

    MetodoPago actualizarMetodo(Integer idMetodo, MetodoPago metodoPago);

    void eliminarMetodo(Integer idMetodo);
}
