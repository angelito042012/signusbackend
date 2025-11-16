package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.UsuarioEmpleado;

public interface UsuarioEmpleadoRepository extends JpaRepository<UsuarioEmpleado, Integer> {
    Optional<UsuarioEmpleado> findByEmail(String email);
}
