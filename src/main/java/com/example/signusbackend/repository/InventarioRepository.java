package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    // Para buscar inventario por producto
    Optional<Inventario> findByProducto_IdProducto(Integer idProducto);

    boolean existsByProducto_IdProducto(Integer idProducto);
}
