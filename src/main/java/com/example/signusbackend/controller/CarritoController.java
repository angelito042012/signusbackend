package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.Carrito;
import com.example.signusbackend.entity.CarritoDetalle;
import com.example.signusbackend.service.CarritoDetalleService;
import com.example.signusbackend.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "*")
public class CarritoController {
    
    private final CarritoService carritoService;
    private final CarritoDetalleService detalleService;

    public CarritoController(CarritoService carritoService, CarritoDetalleService detalleService) {
        this.carritoService = carritoService;
        this.detalleService = detalleService;
    }


    // Listar carritos (admin)
    @GetMapping
    @Operation(summary = "Listar todos los carritos", description = "Obtiene una lista de todos los carritos registrados en el sistema. No deberia de usarse en el frontend.")
    public List<Carrito> listarCarritos() {
        return carritoService.listarCarritos();
    }

    // Crear un carrito para un cliente
    // Aunque el cliente ya deberia de tener un carrito por default
    @PostMapping("/crear/{idCliente}")
    @Operation(summary = "Crear un carrito para un cliente", description = "Crea un nuevo carrito asociado a un cliente específico.")
    public Carrito crearCarrito(@PathVariable Integer idCliente) {
        return carritoService.crearCarrito(idCliente);
    }

    // Obtener carrito por idCliente
    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener carrito por cliente", description = "Obtiene el carrito asociado a un cliente específico utilizando su ID.")
    public Carrito obtenerCarritoPorCliente(@PathVariable Integer idCliente) {
        return carritoService.obtenerCarritoPorCliente(idCliente);
    }

    // Actualizar fecha de modificación
    @PutMapping("/{idCarrito}")
    @Operation(summary = "Actualizar la fecha de modificación de un carrito", description = "Actualiza la fecha de modificación de un carrito específico utilizando su ID.")
    public Carrito actualizarFecha(@PathVariable Integer idCarrito) {
        return carritoService.actualizarFechaModificacion(idCarrito);
    }

    // Eliminar carrito
    @DeleteMapping("/{idCarrito}")
    @Operation(summary = "Eliminar un carrito", description = "Elimina un carrito específico del sistema utilizando su ID. No deberia de usarse en el frontend.")
    public void eliminarCarrito(@PathVariable Integer idCarrito) {
        carritoService.eliminarCarrito(idCarrito);
    }


    //
    // Endpoints para Detalles
    //


    @GetMapping("/{idCarrito}/detalles")
    public List<CarritoDetalle> listarDetalles(@PathVariable Integer idCarrito) {
        return detalleService.listarDetallesPorCarrito(idCarrito);
    }

    @PostMapping("/{idCarrito}/detalles")
    public CarritoDetalle agregarProducto(
            @PathVariable Integer idCarrito,
            @RequestParam Integer idProducto,
            @RequestParam Integer cantidad) {
        return detalleService.agregarProductoAlCarrito(idCarrito, idProducto, cantidad);
    }

    @PutMapping("/{idCarrito}/detalles/{idDetalle}")
    public CarritoDetalle actualizarCantidad(
            @PathVariable Integer idDetalle,
            @RequestParam Integer cantidad) {
        return detalleService.actualizarCantidad(idDetalle, cantidad);
    }

    @DeleteMapping("/{idCarrito}/detalles/{idDetalle}")
    public void eliminarDetalle(@PathVariable Integer idDetalle) {
        detalleService.eliminarDetalle(idDetalle);
    }
    
}
