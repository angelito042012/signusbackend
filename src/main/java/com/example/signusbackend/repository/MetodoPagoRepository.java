package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
    
    //Validar duplicados
    boolean existsByNombre(String nombre);
    Optional<MetodoPago> findByNombre(String nombre);
}
