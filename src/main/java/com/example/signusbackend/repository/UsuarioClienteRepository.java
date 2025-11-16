package com.example.signusbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.UsuarioCliente;

public interface UsuarioClienteRepository extends JpaRepository<UsuarioCliente, Integer> {

    //los metodos estan en ingles por convencion, para poder usarlos con spring data jpa
    Optional<UsuarioCliente> findByEmail(String email);
    Optional<UsuarioCliente> findByOauthProviderAndOauthId(String provider, String oauthId);
}
