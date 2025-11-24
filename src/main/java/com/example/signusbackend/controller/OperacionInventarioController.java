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

import com.example.signusbackend.entity.DetalleOperacionInventario;
import com.example.signusbackend.entity.OperacionInventario;
import com.example.signusbackend.entity.dto.OperacionInventarioRequestDTO;
import com.example.signusbackend.service.DetalleOperacionInventarioService;
import com.example.signusbackend.service.OperacionInventarioService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/operaciones-inventario")
public class OperacionInventarioController {
    private final OperacionInventarioService operacionService;
    private final DetalleOperacionInventarioService detalleService;

    public OperacionInventarioController(OperacionInventarioService operacionService, DetalleOperacionInventarioService detalleService) {
        this.operacionService = operacionService;
        this.detalleService = detalleService;
    }

    // ============================================================
    //      CRUD OPERACION INVENTARIO
    // ============================================================



    @PostMapping("/registrar")
    @Operation(summary = "Registrar operación de inventario con detalles")
    public ResponseEntity<?> registrarOperacion(@RequestBody OperacionInventarioRequestDTO dto) {
        try {
            OperacionInventario operacion = operacionService.registrarOperacionConDetalles(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(operacion);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    /*@PostMapping
    @Operation(summary = "Crear una nueva operación de inventario", description = "Crea una nueva operación de inventario en el sistema.")
    public ResponseEntity<?> crearOperacion(@RequestBody OperacionInventario operacion) {
        OperacionInventario creada = operacionService.crearOperacionInventario(operacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }*/

    @GetMapping
    @Operation(summary = "Listar todas las operaciones de inventario", description = "Devuelve una lista de todas las operaciones de inventario en el sistema.")
    public ResponseEntity<List<OperacionInventario>> listarOperaciones() {
        return ResponseEntity.ok(operacionService.listarOperacionesInventario());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una operación de inventario por ID", description = "Devuelve una operación de inventario específica basada en su ID.")
    public ResponseEntity<?> obtenerOperacion(@PathVariable Integer id) {
        return operacionService.buscarOperacionPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una operación de inventario", description = "Actualiza las propiedades de una operación de inventario existente.")
    public ResponseEntity<?> actualizarOperacion(
            @PathVariable Integer id,
            @RequestBody OperacionInventario operacion) {

        try {
            OperacionInventario actualizada = operacionService.actualizarOperacionInventario(id, operacion);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La operación de inventario no fue encontrada.");
        }
    }



    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una operación de inventario", description = "Elimina una operación de inventario específica basada en su ID.")
    public ResponseEntity<?> eliminarOperacion(@PathVariable Integer id) {
        if (operacionService.buscarOperacionPorId(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La operación de inventario no fue encontrada.");
        }

        operacionService.eliminarOperacionInventario(id);
        return ResponseEntity.ok("Operación eliminada exitosamente.");
    }



    // ============================================================
    //     CRUD DETALLES OPERACION INVENTARIO
    // ============================================================



    @PostMapping("/{idOperacion}/detalles")
    @Operation(summary = "Crear un nuevo detalle de operación de inventario", description = "Crea un nuevo detalle asociado a una operación de inventario específica.")
    public ResponseEntity<?> crearDetalle(
            @PathVariable Integer idOperacion,
            @RequestBody DetalleOperacionInventario detalle) {

        // Asociar correctamente la operación
        OperacionInventario operacion = operacionService.buscarOperacionPorId(idOperacion)
                .orElse(null);

        if (operacion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("La operación de inventario no existe.");
        }

        detalle.setOperacion(operacion);
        DetalleOperacionInventario creado = detalleService.crearDetalle(detalle);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }



    // Obtener todos los detalles de inventario

    @GetMapping("/detalles")
    @Operation(summary = "Listar todos los detalles de operación de inventario", description = "Devuelve una lista de todos los detalles de operación de inventario en el sistema. No se deberia implementar en el frontend.")
    public ResponseEntity<List<DetalleOperacionInventario>> listarDetalles() {
        return ResponseEntity.ok(detalleService.listarDetalles());
    }



    //Obtener detalles por operacion

    @Operation(summary = "Obtener detalles de operación por ID de operación", description = "Devuelve una lista de detalles de operación asociados a una operación de inventario específica.")
    @GetMapping("/operacion/{idOperacion}")
    public ResponseEntity<List<DetalleOperacionInventario>> listarPorOperacion(@PathVariable Integer idOperacion) {
        return ResponseEntity.ok(detalleService.listarPorOperacion(idOperacion));
    }


    @GetMapping("/detalles/{idDetalle}")
    @Operation(summary = "Obtener un detalle de operación por ID", description = "Devuelve un detalle de operación específico basado en su ID.")
    public ResponseEntity<?> obtenerDetalle(@PathVariable Integer idDetalle) {
        return detalleService.buscarPorId(idDetalle)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/detalles/{idDetalle}")
    @Operation(summary = "Actualizar un detalle de operación de inventario", description = "Actualiza las propiedades de un detalle de operación de inventario existente.")
    public ResponseEntity<?> actualizarDetalle(
            @PathVariable Integer idDetalle,
            @RequestBody DetalleOperacionInventario detalle) {

        try {
            DetalleOperacionInventario actualizado = detalleService.actualizarDetalle(idDetalle, detalle);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El detalle de operación no fue encontrado.");
        }
    }



    @DeleteMapping("/detalles/{idDetalle}")
    @Operation(summary = "Eliminar un detalle de operación de inventario", description = "Elimina un detalle de operación de inventario específico basado en su ID.")
    public ResponseEntity<?> eliminarDetalle(@PathVariable Integer idDetalle) {
        if (detalleService.buscarPorId(idDetalle).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El detalle de operación no fue encontrado.");
        }

        detalleService.eliminarDetalle(idDetalle);
        return ResponseEntity.ok("Detalle eliminado exitosamente.");
    }


}
