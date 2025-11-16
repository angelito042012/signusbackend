package com.example.signusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
    // Puedes agregar métodos personalizados de consulta si es necesario
}
