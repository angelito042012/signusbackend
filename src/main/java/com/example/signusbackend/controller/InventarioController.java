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

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {
    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<Inventario> listar() {
        return inventarioService.listarInventario();
    }

    @GetMapping("/{idInventario}")
    public Inventario obtener(@PathVariable Integer idInventario) {
        return inventarioService.obtenerPorId(idInventario);
    }

    @GetMapping("/producto/{idProducto}")
    public Inventario obtenerPorProducto(@PathVariable Integer idProducto) {
        return inventarioService.obtenerPorIdProducto(idProducto);
    }

    @PostMapping
    public Inventario crear(@RequestBody Inventario inventario) {
        return inventarioService.crearInventario(inventario);
    }

    @PutMapping("/{idInventario}")
    public Inventario actualizar(@PathVariable Integer idInventario, @RequestBody Inventario inventario) {
        return inventarioService.actualizarInventario(idInventario, inventario);
    }

    @DeleteMapping("/{idInventario}")
    public void eliminar(@PathVariable Integer idInventario) {
        inventarioService.eliminarInventario(idInventario);
    }
}
