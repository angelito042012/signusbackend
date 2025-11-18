package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Carrito;
import com.example.signusbackend.entity.Cliente;

public interface CarritoRepository extends JpaRepository<Carrito, Integer>{
    // Buscar el carrito activo del cliente
    Optional<Carrito> findByCliente(Cliente cliente);

    // Verificar existencia de carrito para un cliente
    boolean existsByCliente(Cliente cliente);
}
