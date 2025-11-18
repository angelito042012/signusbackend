package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.Inventario;

public interface InventarioService {
    
    List<Inventario> listarInventario();

    Inventario obtenerPorId(Integer idInventario);

    Inventario obtenerPorIdProducto(Integer idProducto);

    Inventario crearInventario(Inventario inventario);

    Inventario actualizarInventario(Integer idInventario, Inventario inventario);

    //No deberia usarse esto
    void eliminarInventario(Integer idInventario);
}
