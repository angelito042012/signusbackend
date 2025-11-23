package com.example.signusbackend.controller;

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

import com.example.signusbackend.entity.MovimientoInventario;
import com.example.signusbackend.service.MovimientoInventarioService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/movimientos-inventario")
public class MovimientoInventarioController {
    private final MovimientoInventarioService service;

    public MovimientoInventarioController(MovimientoInventarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo movimiento de inventario", description = "Crea un nuevo movimiento de inventario en el sistema.")
    public ResponseEntity<?> crearMovimiento(@RequestBody MovimientoInventario movimiento) {
        MovimientoInventario creado = service.crearMovimiento(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    @Operation(summary = "Listar todos los movimientos de inventario", description = "Obtiene una lista de todos los movimientos de inventario registrados en el sistema.")
    public ResponseEntity<List<MovimientoInventario>> listarMovimientos() {
        return ResponseEntity.ok(service.listarMovimientos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un movimiento de inventario por ID", description = "Obtiene los detalles de un movimiento de inventario específico utilizando su ID.")
    public ResponseEntity<?> obtenerMovimiento(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/operacion/{idOperacion}")
    @Operation(summary = "Listar movimientos de inventario por ID de operación", description = "Obtiene una lista de movimientos de inventario asociados a una operación específica utilizando su ID.")
    public ResponseEntity<List<MovimientoInventario>> listarPorOperacion(@PathVariable Integer idOperacion) {
        return ResponseEntity.ok(service.listarPorOperacion(idOperacion));
    }

    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Listar movimientos de inventario por ID de producto", description = "Obtiene una lista de movimientos de inventario asociados a un producto específico utilizando su ID.")
    public ResponseEntity<List<MovimientoInventario>> listarPorProducto(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(service.listarPorProducto(idProducto));
    }

    @GetMapping("/encargado/{idEmpleado}")
    @Operation(summary = "Listar movimientos de inventario por ID de empleado encargado", description = "Obtiene una lista de movimientos de inventario gestionados por un empleado específico utilizando su ID.")
    public ResponseEntity<List<MovimientoInventario>> listarPorEncargado(@PathVariable Integer idEmpleado) {
        return ResponseEntity.ok(service.listarPorEncargado(idEmpleado));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un movimiento de inventario existente", description = "Actualiza los detalles de un movimiento de inventario específico utilizando su ID.")
    public ResponseEntity<?> actualizarMovimiento(
            @PathVariable Integer id,
            @RequestBody MovimientoInventario movimiento) {

        try {
            MovimientoInventario actualizado = service.actualizarMovimiento(id, movimiento);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El movimiento inventario no fue encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un movimiento de inventario", description = "Elimina un movimiento de inventario específico del sistema utilizando su ID. No deberia de usarse para garantizar la integridad de los datos.")
    public ResponseEntity<?> eliminarMovimiento(@PathVariable Integer id) {
        if (service.buscarPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El movimiento inventario no fue encontrado.");
        }

        service.eliminarMovimiento(id);
        return ResponseEntity.ok("Movimiento eliminado exitosamente.");
    }
}
