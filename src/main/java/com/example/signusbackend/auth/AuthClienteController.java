package com.example.signusbackend.auth;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.auth.dto.ClienteRegisterDTO;
import com.example.signusbackend.auth.dto.JwtResponse;
import com.example.signusbackend.auth.dto.LoginRequest;
import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.entity.UsuarioCliente;
import com.example.signusbackend.security.jwt.JwtUtil;
import com.example.signusbackend.service.ClienteService;
import com.example.signusbackend.service.UsuarioClienteService;

@RestController
@RequestMapping("/api/auth")

public class AuthClienteController {
    
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioClienteService usuarioClienteService;
    private final ClienteService clienteService;
    private final PasswordEncoder passwordEncoder;

    public AuthClienteController(AuthenticationManager authManager, JwtUtil jwtUtil, UsuarioClienteService usuarioClienteService, ClienteService clienteService, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioClienteService = usuarioClienteService;
        this.clienteService = clienteService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login/cliente")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            // Rol fijo para cliente
            String token = jwtUtil.generateToken(auth.getName(), "CLIENTE");

            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteRegisterDTO clienteDTO) {

        if (usuarioClienteService.existePorEmail(clienteDTO.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado.");
        }

        UsuarioCliente usuario = new UsuarioCliente();

        usuario.setEmail(clienteDTO.getEmail());
        usuario.setContrasena(passwordEncoder.encode(clienteDTO.getPassword()));
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setEstado("ACTIVO");


        UsuarioCliente usuarioGuardado = usuarioClienteService.registrarUsuarioCliente(usuario);

        Cliente cliente =  new Cliente();

        cliente.setUsuarioCliente(usuarioGuardado);
        clienteService.registrarCliente(cliente);
        

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Cliente registrado exitosamente.");
    }

    

}
