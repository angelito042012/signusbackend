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

import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.service.DetalleVentaService;
import com.example.signusbackend.service.VentaService;

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
    public List<Venta> listar() {
        return ventaService.listarVentas();
    }

    @GetMapping("/{idVenta}")
    public Venta obtener(@PathVariable Integer idVenta) {
        return ventaService.obtenerPorId(idVenta);
    }

    @PostMapping
    public Venta registrar(@RequestBody Venta venta) {
        return ventaService.registrarVenta(venta);
    }

    @PutMapping("/{idVenta}")
    public Venta actualizar(@PathVariable Integer idVenta, @RequestBody Venta venta) {
        return ventaService.actualizarVenta(idVenta, venta);
    }

    @DeleteMapping("/{idVenta}")
    public void eliminar(@PathVariable Integer idVenta) {
        ventaService.eliminarVenta(idVenta);
    }

    // ----------------------------------------------------------------------
    // DETALLES DE LA VENTA
    // ----------------------------------------------------------------------

    @GetMapping("/{idVenta}/detalles")
    public List<DetalleVenta> obtenerDetalles(@PathVariable Integer idVenta) {
        return detalleVentaService.listarPorVenta(idVenta);
    }

    @PostMapping("/{idVenta}/detalles")
    public DetalleVenta agregarDetalle(
            @PathVariable Integer idVenta,
            @RequestBody DetalleVenta detalle) {

        // Asegurar que el detalle se asocie a la venta correcta
        Venta venta = ventaService.obtenerPorId(idVenta);

        detalle.setVenta(venta);

        return detalleVentaService.agregarDetalle(detalle);
    }

    @DeleteMapping("/{idVenta}/detalles/{idDetalle}")
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
