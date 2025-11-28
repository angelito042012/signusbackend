package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    /*@PostMapping("/crear/{idCliente}")
    @Operation(summary = "Crear un carrito para un cliente", description = "Crea un nuevo carrito asociado a un cliente específico.")
    public Carrito crearCarrito(@PathVariable Integer idCliente) {
        return carritoService.crearCarrito(idCliente);
    }*/

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
    @Operation(summary = "Listar detalles de un carrito", description = "Obtiene una lista de todos los detalles (productos) asociados a un carrito específico.")
    public List<CarritoDetalle> listarDetalles(@PathVariable Integer idCarrito) {
        return detalleService.listarDetallesPorCarrito(idCarrito);
    }

    @PostMapping("/{idCarrito}/detalles")
    @Operation(summary = "Agregar un producto al carrito", description = "Agrega un producto específico al carrito con la cantidad indicada.")
    public CarritoDetalle agregarProducto(
            @PathVariable Integer idCarrito,
            @RequestParam Integer idProducto,
            @RequestParam Integer cantidad) {
        return detalleService.agregarProductoAlCarrito(idCarrito, idProducto, cantidad);
    }

    @PutMapping("/{idCarrito}/detalles/{idDetalle}")
    @Operation(summary = "Actualizar la cantidad de un producto en el carrito", description = "Actualiza la cantidad de un producto específico en el carrito.")
    public CarritoDetalle actualizarCantidad(
            @PathVariable Integer idDetalle,
            @RequestParam Integer cantidad) {
        return detalleService.actualizarCantidad(idDetalle, cantidad);
    }

    @DeleteMapping("/{idCarrito}/detalles/{idDetalle}")
    @Operation(summary = "Eliminar un producto del carrito", description = "Elimina un producto específico del carrito utilizando el ID del detalle.")
    public void eliminarDetalle(@PathVariable Integer idDetalle) {
        detalleService.eliminarDetalle(idDetalle);
    }

    @DeleteMapping("/{idCarrito}/detalles/eliminar")
    @Operation(summary = "Eliminar todos los detalles de un carrito", description = "Elimina todos los detalles (productos) asociados a un carrito específico.")
    @ApiResponse(responseCode = "204", description = "Detalles eliminados correctamente")
    @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    public ResponseEntity<Void> eliminarDetallesPorCarrito(@PathVariable Integer idCarrito) {
        detalleService.eliminarDetallesPorCarrito(idCarrito);
        return ResponseEntity.noContent().build();
    }
}
