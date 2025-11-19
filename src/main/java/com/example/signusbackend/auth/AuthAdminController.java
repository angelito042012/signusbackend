package com.example.signusbackend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.example.signusbackend.auth.dto.JwtResponse;
import com.example.signusbackend.auth.dto.LoginRequest;
import com.example.signusbackend.security.jwt.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthAdminController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthAdminController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            // Extraer rol principal (si hay más, ajusta la lógica)
            String role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            String token = jwtUtil.generateToken(auth.getName(), role);

            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception ex) {
            // para otros errores (usuario inactivo, etc.)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
}
