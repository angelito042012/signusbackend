package com.example.signusbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
    // Puedes agregar métodos personalizados de consulta si es necesario
}
