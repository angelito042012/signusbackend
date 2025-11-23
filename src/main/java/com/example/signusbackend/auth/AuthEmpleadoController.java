package com.example.signusbackend.auth;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.auth.dto.EmpleadoRegisterDTO;
import com.example.signusbackend.auth.dto.JwtResponse;
import com.example.signusbackend.auth.dto.LoginRequest;
import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.entity.UsuarioEmpleado;
import com.example.signusbackend.security.jwt.JwtUtil;
import com.example.signusbackend.service.EmpleadoService;
import com.example.signusbackend.service.UsuarioEmpleadoService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthEmpleadoController {
    
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioEmpleadoService usuarioEmpleadoService;
    private final EmpleadoService empleadoService;
    private final PasswordEncoder passwordEncoder;

    public AuthEmpleadoController(
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            UsuarioEmpleadoService usuarioEmpleadoService,
            EmpleadoService empleadoService,
            PasswordEncoder passwordEncoder) {

        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.usuarioEmpleadoService = usuarioEmpleadoService;
        this.empleadoService = empleadoService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login/empleado")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            // Obtener usuarioEmpleado y su rol desde empleado
            UsuarioEmpleado usuario = usuarioEmpleadoService.buscarPorEmail(
                auth.getName()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            Empleado empleado = empleadoService.buscarPorUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Empleado no asociado al usuario"));

            String role = empleado.getRol();  // ADMIN, MANAGER, ETC.

            String token = jwtUtil.generateToken(auth.getName(), role);

            return ResponseEntity.ok(new JwtResponse(token));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register/empleado")
    public ResponseEntity<?> registrarEmpleado(@RequestBody EmpleadoRegisterDTO dto) {

        if (usuarioEmpleadoService.existePorEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado.");
        }

        UsuarioEmpleado usuario = new UsuarioEmpleado();
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(passwordEncoder.encode(dto.getPassword()));
        usuario.setFechaRegistro(new Date());
        usuario.setEstado("ACTIVO");

        Empleado empleado = new Empleado();
        empleado.setNombres(dto.getNombres());
        empleado.setApellidos(dto.getApellidos());
        empleado.setDni(dto.getDni());
        empleado.setTelefono(dto.getTelefono());
        empleado.setRol(dto.getRol()); // ADMIN / EMPLEADO / SUPERVISOR
        empleado.setUsuarioEmpleado(usuario);

        empleadoService.registrarEmpleadoConUsuario(empleado);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Empleado registrado exitosamente.");
    }

}
