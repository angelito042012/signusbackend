package com.example.signusbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.repository.EmpleadoRepository;
import com.example.signusbackend.service.EmpleadoService;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Empleado registrarEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    @Override
    public Empleado actualizarEmpleado(Integer id, Empleado empleado) {
        Empleado existente = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        existente.setNombres(empleado.getNombres());
        existente.setApellidos(empleado.getApellidos());
        existente.setDni(empleado.getDni());
        existente.setTelefono(empleado.getTelefono());
        existente.setRol(empleado.getRol());

        return empleadoRepository.save(existente);
    }

    @Override
    public void eliminarEmpleado(Integer id) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado");
        }

        empleadoRepository.deleteById(id);
    }

    @Override
    public Optional<Empleado> obtenerPorUsuario(Integer idUsuario) {
        return empleadoRepository.findByUsuarioEmpleadoIdUsuario(idUsuario);
    }

}
