package com.example.signusbackend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.UsuarioCliente;
import com.example.signusbackend.service.UsuarioClienteService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/usuarios-clientes")
public class UsuarioClienteController {

    private final UsuarioClienteService usuarioClienteService;

    public UsuarioClienteController(UsuarioClienteService usuarioClienteService) {
        this.usuarioClienteService = usuarioClienteService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario cliente por ID", description = "Obtiene los detalles de un usuario cliente específico utilizando su ID.")
    public ResponseEntity<UsuarioCliente> obtenerPorId(@PathVariable Integer id) {
        Optional<UsuarioCliente> usuario = usuarioClienteService.findById(id);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioCliente> findByEmail(@PathVariable String email) {
        Optional<UsuarioCliente> usuario = usuarioClienteService.findByEmail(email);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios de los clientes", description = "Obtiene una lista de todos los usuarios clientes registrados en el sistema.")
    public ResponseEntity<List<UsuarioCliente>> listarTodos() {
        List<UsuarioCliente> usuarios = usuarioClienteService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario cliente", description = "Elimina un usuario cliente específico del sistema utilizando su ID. No deberia usarse en el frontend.")
    public ResponseEntity<Void> eliminarUsuarioCliente(@PathVariable Integer id) {
        usuarioClienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/desactivar/{id}")
    @Operation(summary = "Desactivar un usuario cliente", description = "Desactiva un usuario cliente específico del sistema utilizando su ID.")
    public ResponseEntity<Void> desactivarUsuarioCliente(@PathVariable Integer id) {
        try {
            usuarioClienteService.desactivarUsuarioCliente(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Verificar existencia de usuario cliente por email", description = "Verifica si un usuario cliente existe en el sistema utilizando su email.")
    public ResponseEntity<Boolean> existePorEmail(@PathVariable String email) {
        boolean exists = usuarioClienteService.existePorEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/oauth")
    @Operation(summary = "Obtener un usuario cliente por proveedor OAuth y ID OAuth", description = "Obtiene los detalles de un usuario cliente específico utilizando su proveedor OAuth y su ID OAuth.")
    public ResponseEntity<UsuarioCliente> findByOauthProviderAndOauthId(
            @RequestParam String provider, @RequestParam String oauthId) {
        Optional<UsuarioCliente> usuario = usuarioClienteService.findByOauthProviderAndOauthId(provider, oauthId);
        return usuario.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
