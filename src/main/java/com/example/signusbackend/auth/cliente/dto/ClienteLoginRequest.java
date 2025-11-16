package com.example.signusbackend.auth.cliente.dto;

import lombok.Data;

@Data
public class ClienteLoginRequest {
    private String email;
    private String password;
}
