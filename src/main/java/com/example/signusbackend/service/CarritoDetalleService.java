package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.CarritoDetalle;

public interface CarritoDetalleService {
    
    List<CarritoDetalle> listarDetallesPorCarrito(Integer idCarrito);

    CarritoDetalle agregarProductoAlCarrito(Integer idCarrito, Integer idProducto, Integer cantidad);

    CarritoDetalle actualizarCantidad(Integer idDetalle, Integer cantidad);

    void eliminarDetalle(Integer idDetalle);

}
