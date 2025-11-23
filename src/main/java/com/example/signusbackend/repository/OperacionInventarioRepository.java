package com.example.signusbackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.OperacionInventario;

public interface OperacionInventarioRepository extends JpaRepository<OperacionInventario, Integer> {
    // Métodos específicos de OperacionInventario (si los hay)
}
