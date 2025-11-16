package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.UsuarioEmpleado;

public interface UsuarioEmpleadoService {
    
    Optional<UsuarioEmpleado> findByEmail(String email);

    UsuarioEmpleado save(UsuarioEmpleado usuario);

    List<UsuarioEmpleado> findAll();
    Optional<UsuarioEmpleado> findById(Integer id);
}
