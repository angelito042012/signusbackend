package com.example.signusbackend.auth;

import java.util.Date;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.signusbackend.auth.dto.AdminRegisterDTO;
import com.example.signusbackend.auth.dto.JwtResponse;
import com.example.signusbackend.auth.dto.LoginRequest;
import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.entity.UsuarioEmpleado;
import com.example.signusbackend.security.jwt.JwtUtil;
import com.example.signusbackend.service.EmpleadoService;
import com.example.signusbackend.service.UsuarioEmpleadoService;

@RestController
@RequestMapping("/api/auth")
public class AuthAdminController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    private final UsuarioEmpleadoService usuarioEmpleadoService;
    private final EmpleadoService empleadoService;
    private final PasswordEncoder passwordEncoder;

    public AuthAdminController(AuthenticationManager authManager, JwtUtil jwtUtil, UsuarioEmpleadoService usuarioEmpleadoService, EmpleadoService empleadoService, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioEmpleadoService = usuarioEmpleadoService;
        this.empleadoService = empleadoService;
        this.passwordEncoder = passwordEncoder;
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

    @PostMapping("/register-admin")
    public ResponseEntity<String> registrarAdmin(@RequestBody AdminRegisterDTO adminDTO) {
        // Validar si el email ya existe
        if (usuarioEmpleadoService.existePorEmail(adminDTO.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado.");
        }

        // Crear el usuario empleado (credenciales)
        UsuarioEmpleado usuarioEmpleado = new UsuarioEmpleado();
        usuarioEmpleado.setEmail(adminDTO.getEmail());
        usuarioEmpleado.setContrasena(passwordEncoder.encode(adminDTO.getPassword())); // Encriptar la contraseña
        usuarioEmpleado.setFechaRegistro(new Date());
        usuarioEmpleado.setEstado("ACTIVO"); // Activar por defecto

        // Crear el empleado (datos personales)
        Empleado empleado = new Empleado();
        empleado.setNombres(adminDTO.getNombres());
        empleado.setApellidos(adminDTO.getApellidos());
        empleado.setDni(adminDTO.getDni());
        empleado.setTelefono(adminDTO.getTelefono());
        empleado.setRol("ADMIN");
        empleado.setUsuarioEmpleado(usuarioEmpleado); // Relación con UsuarioEmpleado

        // Guardar en la base de datos
        empleadoService.registrarEmpleadoConUsuario(empleado);

        return ResponseEntity.status(HttpStatus.CREATED).body("Administrador registrado exitosamente.");
    }



}
