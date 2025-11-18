package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.Empleado;

public interface EmpleadoService {
    List<Empleado> listarEmpleados();

    Empleado registrarEmpleado(Empleado empleado);

    Empleado actualizarEmpleado(Integer id, Empleado empleado);

    void eliminarEmpleado(Integer id);

    Optional<Empleado> obtenerPorUsuario(Integer idUsuario);
}
