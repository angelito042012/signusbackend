package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.UsuarioEmpleado;

public interface UsuarioEmpleadoService {
    
    Optional<UsuarioEmpleado> buscarPorEmail(String email);

    UsuarioEmpleado guardarEmpleado(UsuarioEmpleado usuario);

    List<UsuarioEmpleado> listarEmpleados();

    Optional<UsuarioEmpleado> buscarPorId(Integer id);

    void eliminarPorId(Integer id);

    boolean existePorEmail(String email);

    UsuarioEmpleado registrarUsuarioEmpleado(UsuarioEmpleado usuarioEmpleado);

    void desactivarUsuarioEmpleado(Integer id);

}
