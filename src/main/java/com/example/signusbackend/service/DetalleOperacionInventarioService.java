package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.DetalleOperacionInventario;

public interface DetalleOperacionInventarioService {
    
    DetalleOperacionInventario crearDetalle(DetalleOperacionInventario detalle);

    List<DetalleOperacionInventario> listarDetalles();

    Optional<DetalleOperacionInventario> buscarPorId(Integer idDetalle);

    List<DetalleOperacionInventario> listarPorOperacion(Integer idOperacion);

    DetalleOperacionInventario actualizarDetalle(Integer idDetalle, DetalleOperacionInventario detalleNuevo);

    void eliminarDetalle(Integer idDetalle);
}
