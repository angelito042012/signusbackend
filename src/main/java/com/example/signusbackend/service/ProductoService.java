package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.Producto;

public interface ProductoService {
    List<Producto> listarProductos();
    Producto obtenerProducto(Integer id);
    Producto crearProducto(Producto producto);
    Producto actualizarProducto(Integer id, Producto producto);
    void eliminarProducto(Integer id);
    List<Producto> listarPorCategoria(Integer idCategoria);
}
