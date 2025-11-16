package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.UsuarioCliente;

public interface UsuarioClienteService {

    Optional<UsuarioCliente> findByEmail(String email);

    Optional<UsuarioCliente> findByOauthProviderAndOauthId(String provider, String oauthId);

    UsuarioCliente save(UsuarioCliente usuario);

    List<UsuarioCliente> findAll();

    Optional<UsuarioCliente> findById(Integer id);
}