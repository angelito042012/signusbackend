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

@RestController
@RequestMapping("/api/metodos-pago")
@CrossOrigin(origins = "*")
public class MetodoPagoController {
    
    private final MetodoPagoService metodoPagoService;

    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    public List<MetodoPago> listar() {
        return metodoPagoService.listarMetodos();
    }

    @GetMapping("/{idMetodo}")
    public MetodoPago obtenerPorId(@PathVariable Integer idMetodo) {
        return metodoPagoService.obtenerPorId(idMetodo);
    }

    @PostMapping
    public MetodoPago crear(@RequestBody MetodoPago metodoPago) {
        return metodoPagoService.crearMetodo(metodoPago);
    }

    @PutMapping("/{idMetodo}")
    public MetodoPago actualizar(@PathVariable Integer idMetodo, @RequestBody MetodoPago metodoPago) {
        return metodoPagoService.actualizarMetodo(idMetodo, metodoPago);
    }

    @DeleteMapping("/{idMetodo}")
    public void eliminar(@PathVariable Integer idMetodo) {
        metodoPagoService.eliminarMetodo(idMetodo);
    }
}
