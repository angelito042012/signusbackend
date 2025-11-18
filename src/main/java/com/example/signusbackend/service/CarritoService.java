package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.Carrito;

public interface CarritoService {
    List<Carrito> listarCarritos();
    Carrito crearCarrito(Integer idCliente);
    Carrito obtenerCarritoPorCliente(Integer idCliente);
    Carrito actualizarFechaModificacion(Integer idCarrito);
    void eliminarCarrito(Integer idCarrito);
}
