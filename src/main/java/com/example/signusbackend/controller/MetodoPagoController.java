package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.MetodoPago;
import com.example.signusbackend.service.MetodoPagoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/metodos-pago")
@CrossOrigin(origins = "*")
public class MetodoPagoController {
    
    private final MetodoPagoService metodoPagoService;

    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los métodos de pago", description = "Obtiene una lista de todos los métodos de pago registrados en el sistema.")
    public List<MetodoPago> listar() {
        return metodoPagoService.listarMetodos();
    }

    @GetMapping("/{idMetodo}")
    @Operation(summary = "Obtener un método de pago por ID", description = "Obtiene los detalles de un método de pago específico utilizando su ID.")
    public MetodoPago obtenerPorId(@PathVariable Integer idMetodo) {
        return metodoPagoService.obtenerPorId(idMetodo);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo método de pago", description = "Crea un nuevo método de pago en el sistema.")
    public MetodoPago crear(@RequestBody MetodoPago metodoPago) {
        return metodoPagoService.crearMetodo(metodoPago);
    }

    @PutMapping("/{idMetodo}")
    @Operation(summary = "Actualizar un método de pago existente", description = "Actualiza los detalles de un método de pago específico utilizando su ID.")
    public MetodoPago actualizar(@PathVariable Integer idMetodo, @RequestBody MetodoPago metodoPago) {
        return metodoPagoService.actualizarMetodo(idMetodo, metodoPago);
    }

    @DeleteMapping("/{idMetodo}")
    @Operation(summary = "Eliminar un método de pago", description = "Elimina un método de pago específico del sistema utilizando su ID.")
    public void eliminar(@PathVariable Integer idMetodo) {
        metodoPagoService.eliminarMetodo(idMetodo);
    }
}
