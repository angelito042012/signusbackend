package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.service.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "*")
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
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
}
