package com.example.signusbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.DetalleOperacionInventario;

public interface DetalleOperacionInventarioRepository extends JpaRepository<DetalleOperacionInventario, Integer> {
    List<DetalleOperacionInventario> findByOperacionIdOperacion(Integer idOperacion);
    
}
