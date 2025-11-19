package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    // Muy útil para autenticación y obtener info rápidamente
    Optional<Empleado> findByUsuarioEmpleado_IdUsuario(Integer idUsuario);
}
