package com.example.signusbackend.controller;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.UsuarioEmpleado;
import com.example.signusbackend.service.UsuarioEmpleadoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/usuarios-empleados")
public class UsuarioEmpleadoController {
    private final UsuarioEmpleadoService service;

    public UsuarioEmpleadoController(UsuarioEmpleadoService service) {
        this.service = service;
    }

    // -------------------------------
    // LISTAR TODOS LOS USUARIOS
    // -------------------------------
    @GetMapping
    @Operation(summary = "Listar todos los usuarios de los empleados", description = "Obtiene una lista de todos los usuarios empleados registrados en el sistema.")
    public ResponseEntity<List<UsuarioEmpleado>> listarEmpleados() {
        return ResponseEntity.ok(service.listarEmpleados());
    }

    // -------------------------------
    // BUSCAR POR ID
    // -------------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario empleado por ID", description = "Obtiene los detalles de un usuario empleado específico utilizando su ID.")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // -------------------------------
    // CREAR NUEVO USUARIO EMPLEADO
    // -------------------------------
    /*@PostMapping
    @Operation(summary = "Crear un nuevo usuario empleado", description = "Crea un nuevo usuario empleado en el sistema. No deberia usarse en el frontend")
    public ResponseEntity<?> crearEmpleado(@RequestBody UsuarioEmpleado usuario) {

        if (service.existePorEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body("El email ya está registrado.");
        }

        usuario.setFechaRegistro(new Date());
        usuario.setEstado("ACTIVO");

        UsuarioEmpleado nuevo = service.guardarEmpleado(usuario);
        return ResponseEntity.ok(nuevo);
    }*/

    // -------------------------------
    // ACTUALIZAR USUARIO EMPLEADO
    // -------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario empleado", description = "Actualiza los detalles de un usuario empleado específico utilizando su ID.")
    public ResponseEntity<?> actualizarEmpleado(
            @PathVariable Integer id,
            @RequestBody UsuarioEmpleado datos) {

        return service.buscarPorId(id)
                .map(usuario -> {
                    usuario.setEmail(datos.getEmail());
                    usuario.setContrasena(datos.getContrasena());
                    usuario.setEstado(datos.getEstado());
                    return ResponseEntity.ok(service.guardarEmpleado(usuario));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }



    // -------------------------------
    // ELIMINAR USUARIO EMPLEADO (FÍSICO)
    // -------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario empleado", description = "Elimina un usuario empleado específico del sistema utilizando su ID. No deberia usarse en el frontend.")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Integer id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.badRequest().body("El usuario empleado no existe.");
        }

        service.eliminarPorId(id);
        return ResponseEntity.ok("Usuario empleado eliminado correctamente.");
    }



    // -------------------------------
    // DESACTIVAR USUARIO
    // -------------------------------
    @PutMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar un usuario empleado", description = "Desactiva un usuario empleado específico del sistema utilizando su ID.")
    public ResponseEntity<?> desactivarEmpleado(@PathVariable Integer id) {

        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.badRequest().body("El usuario empleado no existe.");
        }

        service.desactivarUsuarioEmpleado(id);
        return ResponseEntity.ok("Usuario empleado desactivado correctamente.");
    }
}
