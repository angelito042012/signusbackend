package com.example.signusbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.UsuarioCliente;
import com.example.signusbackend.repository.UsuarioClienteRepository;
import com.example.signusbackend.service.UsuarioClienteService;

@Service
public class UsuarioClienteServiceImpl implements UsuarioClienteService{

    //Se utilizara la variable repo para acceder a los metodos del repositorio facilmente

    private final UsuarioClienteRepository repo;

    public UsuarioClienteServiceImpl(UsuarioClienteRepository repo) {
        this.repo = repo;
    }



    @Override
    public Optional<UsuarioCliente> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public Optional<UsuarioCliente> findByOauthProviderAndOauthId(String provider, String oauthId) {
        return repo.findByOauthProviderAndOauthId(provider, oauthId);
    }

    @Override
    public List<UsuarioCliente> findAll() {
        return repo.findAll();
    }

    @Override
    public Optional<UsuarioCliente> findById(Integer id) {
        return repo.findById(id);
    }



    @Override
    public boolean existePorEmail(String email) {
        return repo.findByEmail(email).isPresent();
    }



    @Override
    public UsuarioCliente registrarUsuarioCliente(UsuarioCliente usuario) {
        return repo.save(usuario);
    }



    @Override
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }



    @Override
    public void desactivarUsuarioCliente(Integer id) {
        Optional<UsuarioCliente> usuarioOpt = repo.findById(id);
        if (usuarioOpt.isPresent()) {
            UsuarioCliente usuario = usuarioOpt.get();
            usuario.setEstado("INACTIVO"); // Suponiendo que "INACTIVO" es el estado para desactivar
            repo.save(usuario);
        } else {
            throw new RuntimeException("Usuario Cliente no encontrado");
        }
    }

}