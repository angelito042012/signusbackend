package com.example.signusbackend.auth.cliente;

import com.example.signusbackend.auth.cliente.dto.ClienteLoginRequest;
import com.example.signusbackend.auth.cliente.dto.ClienteRegisterRequest;
import com.example.signusbackend.entity.UsuarioCliente;

public interface ClienteAuthService {
    String login(ClienteLoginRequest request);

    UsuarioCliente register(ClienteRegisterRequest request);
}
