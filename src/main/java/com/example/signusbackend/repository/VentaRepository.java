package com.example.signusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Integer> {
    //aca se pueden implementar otros metodos
}
