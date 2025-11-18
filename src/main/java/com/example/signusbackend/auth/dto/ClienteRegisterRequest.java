package com.example.signusbackend.auth.dto;

import lombok.Data;

@Data
public class ClienteRegisterRequest {
    private String email;
    private String password;

    // Supuestamente, estos son los campos del cliente, aun no estoy seguro de como colocarlos en la tabla
    // de usuario_cliente y cliente

    private String nombres;
    private String apellidos;
    private String dni;
    private String direccion;
    private String telefono;
}
