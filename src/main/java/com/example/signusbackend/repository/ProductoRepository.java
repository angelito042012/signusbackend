package com.example.signusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Producto;
import java.util.List;


public interface ProductoRepository extends JpaRepository<Producto, Integer>{
    // Puedes agregar métodos personalizados de consulta si es necesario

    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoria_IdCategoria(Integer idCategoria);
    
}
