package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.entity.dto.VentaRegistrarDTO;
import com.example.signusbackend.service.DetalleVentaService;
import com.example.signusbackend.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final VentaService ventaService;
    private final DetalleVentaService detalleVentaService;

    public VentaController(VentaService ventaService, DetalleVentaService detalleVentaService) {
        this.ventaService = ventaService;
        this.detalleVentaService = detalleVentaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las ventas", description = "Obtiene una lista de todas las ventas registradas en el sistema.")
    public List<Venta> listar() {
        return ventaService.listarVentas();
    }

    @GetMapping("/{idVenta}")
    @Operation(summary = "Obtener una venta por ID", description = "Obtiene los detalles de una venta específica utilizando su ID.")
    public Venta obtener(@PathVariable Integer idVenta) {
        return ventaService.obtenerPorId(idVenta);
    }

    //REGISTRAR VENTA FISICA

    @PostMapping("/registrar")
    @Operation(summary = "Registrar una nueva venta física", description = "Registra una nueva venta en el sistema.")
    public ResponseEntity<?> registrarVenta(@RequestBody VentaRegistrarDTO dto) {
        try {
            Venta venta = ventaService.registrarVentaFisica(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(venta);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }



    @PutMapping("/{idVenta}")
    @Operation(summary = "Actualizar una venta existente", description = "Actualiza los detalles de una venta específica utilizando su ID.")
    public Venta actualizar(@PathVariable Integer idVenta, @RequestBody Venta venta) {
        return ventaService.actualizarVenta(idVenta, venta);
    }

    @DeleteMapping("/{idVenta}")
    @Operation(summary = "Eliminar una venta", description = "Elimina una venta específica del sistema utilizando su ID.")
    public void eliminar(@PathVariable Integer idVenta) {
        ventaService.eliminarVenta(idVenta);
    }

    // ----------------------------------------------------------------------
    // DETALLES DE LA VENTA
    // ----------------------------------------------------------------------

    @GetMapping("/{idVenta}/detalles")
    @Operation(summary = "Listar detalles de una venta", description = "Obtiene una lista de todos los detalles asociados a una venta específica.")
    public List<DetalleVenta> obtenerDetalles(@PathVariable Integer idVenta) {
        return detalleVentaService.listarPorVenta(idVenta);
    }

    @PostMapping("/{idVenta}/detalles")
    @Operation(summary = "Agregar un detalle a una venta", description = "Agrega un nuevo detalle a una venta específica.")
    public DetalleVenta agregarDetalle(
            @PathVariable Integer idVenta,
            @RequestBody DetalleVenta detalle) {

        // Asegurar que el detalle se asocie a la venta correcta
        Venta venta = ventaService.obtenerPorId(idVenta);

        detalle.setVenta(venta);

        return detalleVentaService.agregarDetalle(detalle);
    }

    @DeleteMapping("/{idVenta}/detalles/{idDetalle}")
    @Operation(summary = "Eliminar un detalle de una venta", description = "Elimina un detalle específico de una venta.")
    public void eliminarDetalle(
            @PathVariable Integer idVenta,
            @PathVariable Integer idDetalle) {

        // Seguridad adicional (no eliminar detalles ajenos)
        List<DetalleVenta> detalles = detalleVentaService.listarPorVenta(idVenta);
        boolean existe = detalles.stream()
                .anyMatch(d -> d.getIdDetalleVenta().equals(idDetalle));

        if (!existe) {
            throw new RuntimeException("El detalle no pertenece a esta venta");
        }

        detalleVentaService.eliminarDetalle(idDetalle);
    }

}
