package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.signusbackend.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
    // Puedes agregar métodos personalizados de consulta si es necesario

    // Buscar cliente por el email de su UsuarioCliente
    @Query("SELECT c FROM Cliente c WHERE c.usuarioCliente.email = :email")
    Optional<Cliente> findByUsuarioClienteEmail(String email);
}
