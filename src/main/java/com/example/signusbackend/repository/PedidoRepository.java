package com.example.signusbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByEstado(String estado);
}
