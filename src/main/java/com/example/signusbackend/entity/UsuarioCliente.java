package com.example.signusbackend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario_cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;

    @Column(unique = true, nullable = false)
    private String email;
    private String contrasena;

    //si vamos a requerir usar propiedades que tienen guiones bajos pára los servicios
    //es mejor renombrar las variables a camel case, y llamar a columna

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "oauth_id")
    private String oauthId;

    private LocalDateTime fecha_registro;
    private String estado;
}

/*
 * @Entity
@Table(name = "usuario_sistema")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSistema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String email;
    private String contrasena;
    private String estado;
}
 */