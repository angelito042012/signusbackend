package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.entity.UsuarioEmpleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    // Muy útil para autenticación y obtener info rápidamente
    Optional<Empleado> findByUsuarioEmpleado_IdUsuario(Integer idUsuario);

    Optional<Empleado> findByUsuarioEmpleado(UsuarioEmpleado usuarioEmpleado);

    Optional<Empleado> findByUsuarioEmpleado_Email(String email); // Nuevo método para buscar por email
    
}
