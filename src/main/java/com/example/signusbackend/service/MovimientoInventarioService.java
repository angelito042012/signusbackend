package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.MovimientoInventario;

public interface MovimientoInventarioService {
    MovimientoInventario crearMovimiento(MovimientoInventario movimiento);

    List<MovimientoInventario> listarMovimientos();

    Optional<MovimientoInventario> buscarPorId(Integer idMovimiento);

    List<MovimientoInventario> listarPorOperacion(Integer idOperacion);

    List<MovimientoInventario> listarPorProducto(Integer idProducto);

    List<MovimientoInventario> listarPorEncargado(Integer idEmpleado);

    MovimientoInventario actualizarMovimiento(Integer idMovimiento, MovimientoInventario movimiento);

    void eliminarMovimiento(Integer idMovimiento);
}
