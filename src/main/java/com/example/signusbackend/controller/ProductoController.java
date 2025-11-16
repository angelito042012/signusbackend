package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.Producto;
import com.example.signusbackend.service.ProductoService;

@RestController
@RequestMapping("/api/productos")

@CrossOrigin(origins = "*") // Permitir solicitudes desde cualquier origen por ahora, luego restringir según sea necesario

public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    
    @GetMapping
    public List<Producto> listar() {
        return productoService.listarProductos();
    }
    
}
