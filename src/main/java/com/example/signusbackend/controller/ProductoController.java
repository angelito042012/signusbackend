package com.example.signusbackend.controller;

import java.util.List;

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

import com.example.signusbackend.entity.Producto;
import com.example.signusbackend.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/productos")

@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen por ahora, luego restringir según sea necesario

public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    
    // LISTAR
    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Obtiene una lista de todos los productos registrados en el sistema.")
    public List<Producto> listar() {
        return productoService.listarProductos();
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID", description = "Obtiene los detalles de un producto específico utilizando su ID.")
    public ResponseEntity<Producto> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.obtenerProducto(id));
    }

    // CREAR
    @PostMapping("/crear")
    @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el sistema y genera un inventario inicial para el producto.")
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.crearProducto(producto));
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto existente", description = "Actualiza los detalles de un producto específico utilizando su ID.")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Integer id,
            @RequestBody Producto producto
    ) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto específico del sistema utilizando su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // FILTRAR POR CATEGORIA
    @GetMapping("/categoria/{idCategoria}")
    @Operation(summary = "Listar productos por categoría", description = "Obtiene una lista de productos filtrados por una categoría específica utilizando su ID.")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable Integer idCategoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(idCategoria));
    }
    
}
