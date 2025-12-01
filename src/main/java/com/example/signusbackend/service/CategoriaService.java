package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.Categoria;

public interface CategoriaService {
    List<Categoria> listarCategorias();
    Categoria obtenerCategoria(Integer id);
    Categoria crearCategoria(Categoria categoria);
    Categoria actualizarCategoria(Integer id, Categoria categoria);
    void eliminarCategoria(Integer id);
    Categoria obtenerCategoriaPorNombre(String nombre);
}
