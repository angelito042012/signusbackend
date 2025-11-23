package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.UsuarioCliente;

public interface UsuarioClienteService {

    Optional<UsuarioCliente> findByEmail(String email);

    Optional<UsuarioCliente> findByOauthProviderAndOauthId(String provider, String oauthId);

    List<UsuarioCliente> findAll();

    Optional<UsuarioCliente> findById(Integer id);

    boolean existePorEmail(String email);

    UsuarioCliente registrarUsuarioCliente(UsuarioCliente usuario);

    //esto no deberia de usarse
    void deleteById(Integer id);

    //metodo para desactivar un usuario cliente
    void desactivarUsuarioCliente(Integer id);
}