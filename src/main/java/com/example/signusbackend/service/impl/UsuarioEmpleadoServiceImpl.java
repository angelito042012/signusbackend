package com.example.signusbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.UsuarioEmpleado;
import com.example.signusbackend.repository.UsuarioEmpleadoRepository;
import com.example.signusbackend.service.UsuarioEmpleadoService;

@Service
public class UsuarioEmpleadoServiceImpl implements UsuarioEmpleadoService {


    private final UsuarioEmpleadoRepository repo;

    public UsuarioEmpleadoServiceImpl(UsuarioEmpleadoRepository repo) {
        this.repo = repo;
    }


    @Override
    public boolean existePorEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }

    @Override
    public Optional<UsuarioEmpleado> buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public UsuarioEmpleado guardarEmpleado(UsuarioEmpleado usuario) {
        return repo.save(usuario);
    }

    @Override
    public List<UsuarioEmpleado> listarEmpleados() {
        return repo.findAll();
    }

    @Override
    public Optional<UsuarioEmpleado> buscarPorId(Integer id) {
        return repo.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public void desactivarUsuarioEmpleado(Integer id) {
        Optional<UsuarioEmpleado> usuarioOpt = repo.findById(id);
        if (usuarioOpt.isPresent()) {
            UsuarioEmpleado usuario = usuarioOpt.get();
            usuario.setEstado("INACTIVO"); // Suponiendo que hay un campo 'activo' en la entidad
            repo.save(usuario);
        }
    }


    @Override
    public UsuarioEmpleado registrarUsuarioEmpleado(UsuarioEmpleado usuarioEmpleado) {
        return repo.save(usuarioEmpleado);
    }
    
}
