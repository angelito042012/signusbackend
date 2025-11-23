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

import com.example.signusbackend.entity.Inventario;
import com.example.signusbackend.service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todo el inventario", description = "Obtiene una lista de todos los registros de inventario en el sistema.")
    public List<Inventario> listar() {
        return inventarioService.listarInventario();
    }

    @GetMapping("/{idInventario}")
    @Operation(summary = "Obtener un registro de inventario por ID", description = "Obtiene los detalles de un registro de inventario específico utilizando su ID.")
    public Inventario obtener(@PathVariable Integer idInventario) {
        return inventarioService.obtenerPorId(idInventario);
    }

    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Obtener inventario por ID de producto", description = "Obtiene el registro de inventario asociado a un producto específico utilizando su ID.")
    public Inventario obtenerPorProducto(@PathVariable Integer idProducto) {
        return inventarioService.obtenerPorIdProducto(idProducto);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo registro de inventario", description = "Crea un nuevo registro de inventario en el sistema.")
    public Inventario crear(@RequestBody Inventario inventario) {
        return inventarioService.crearInventario(inventario);
    }

    @PutMapping("/{idInventario}")
    @Operation(summary = "Actualizar un registro de inventario existente", description = "Actualiza los detalles de un registro de inventario específico utilizando su ID. Deberia usarse los endpoints de operacion-inventario-controller")
    public Inventario actualizar(@PathVariable Integer idInventario, @RequestBody Inventario inventario) {
        return inventarioService.actualizarInventario(idInventario, inventario);
    }

    @DeleteMapping("/{idInventario}")
    @Operation(summary = "Eliminar un registro de inventario", description = "Elimina un registro de inventario específico del sistema utilizando su ID. No deberia usarse en el frontend.")
    public void eliminar(@PathVariable Integer idInventario) {
        inventarioService.eliminarInventario(idInventario);
    }
}
