package com.example.signusbackend.auth.dto;

import lombok.Data;

@Data
public class ClienteLoginRequest {
    private String email;
    private String password;
}
