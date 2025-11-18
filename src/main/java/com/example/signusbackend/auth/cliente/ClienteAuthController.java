package com.example.signusbackend.auth.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.auth.dto.ClienteLoginRequest;
import com.example.signusbackend.auth.dto.ClienteRegisterRequest;


@RestController
@RequestMapping("/api/auth/cliente")
public class ClienteAuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClienteAuthService authService;


    // =====================
    // LOGIN CLIENTE
    // =====================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody ClienteLoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Si llegó aquí, las credenciales son correctas
        String token = authService.login(request);  // Más adelante devolverá JWT

        return ResponseEntity.ok(token);
    }


    // =====================
    // REGISTRO CLIENTE
    // =====================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody ClienteRegisterRequest request) {

        authService.register(request);

        return ResponseEntity.ok("Cliente registrado correctamente");
    }

    /*
    @PostMapping("/cliente/login")
    public ResponseEntity<String> login(@RequestBody ClienteLoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }
        */
}
