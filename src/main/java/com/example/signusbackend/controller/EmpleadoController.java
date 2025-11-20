package com.example.signusbackend.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.auth.dto.AdminRegisterDTO;
import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.entity.UsuarioEmpleado;
import com.example.signusbackend.service.EmpleadoService;
import com.example.signusbackend.service.UsuarioEmpleadoService;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
public class EmpleadoController {
    private final EmpleadoService empleadoService;
    private final UsuarioEmpleadoService usuarioEmpleadoService;
    private final BCryptPasswordEncoder passwordEncoder;

    public EmpleadoController(EmpleadoService empleadoService, UsuarioEmpleadoService usuarioEmpleadoService, BCryptPasswordEncoder passwordEncoder) {
        this.empleadoService = empleadoService;
        this.usuarioEmpleadoService = usuarioEmpleadoService;
        this.passwordEncoder = passwordEncoder;
    }

    // ========================
    // LISTAR EMPLEADOS
    // ========================
    @GetMapping
    public ResponseEntity<List<Empleado>> listarEmpleados() {
        return ResponseEntity.ok(empleadoService.listarEmpleados());
    }

    // ========================
    // REGISTRAR EMPLEADO
    // ========================
    @PostMapping
    public ResponseEntity<Empleado> registrarEmpleado(@RequestBody Empleado empleado) {

        // opcional: validar campos requeridos
        if (empleado.getNombres() == null || empleado.getApellidos() == null) {
            return ResponseEntity.badRequest().build();
        }

        Empleado nuevo = empleadoService.registrarEmpleado(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // ========================
    // ACTUALIZAR EMPLEADO
    // ========================
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(
            @PathVariable Integer id,
            @RequestBody Empleado empleadoActualizado) {

        Empleado actualizado = empleadoService.actualizarEmpleado(id, empleadoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    // ========================
    // ELIMINAR EMPLEADO
    // ========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }

    // ========================
    // BUSCAR EMPLEADO POR idUsuario (FK)
    // ========================
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Empleado> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        return empleadoService.obtenerPorUsuario(idUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    /*@PostMapping("/register-admin")
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
    }*/
}
