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
    public Optional<UsuarioEmpleado> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public UsuarioEmpleado save(UsuarioEmpleado usuario) {
        return repo.save(usuario);
    }

    @Override
    public List<UsuarioEmpleado> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<UsuarioEmpleado> findById(Integer id) {
        return repo.findById(id);
    }
    
}
