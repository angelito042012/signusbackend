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

import com.example.signusbackend.entity.Categoria;
import com.example.signusbackend.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/categorias")

@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen por ahora, luego restringir según sea necesario

public class CategoriaController {
    
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // LISTAR
    @GetMapping
    @Operation(summary = "Listar todas las categorías", description = "Obtiene una lista de todas las categorías registradas en el sistema.")
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por ID", description = "Obtiene los detalles de una categoría específica utilizando su ID.")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.obtenerCategoria(id));
    }

    // CREAR
    @PostMapping
    @Operation(summary = "Crear una nueva categoría", description = "Crea una nueva categoría en el sistema.")
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.crearCategoria(categoria));
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría existente", description = "Actualiza los detalles de una categoría específica utilizando su ID.")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoria));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría", description = "Elimina una categoría específica del sistema utilizando su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
