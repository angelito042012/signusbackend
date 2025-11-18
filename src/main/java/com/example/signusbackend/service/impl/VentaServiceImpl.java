package com.example.signusbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.repository.VentaRepository;
import com.example.signusbackend.service.VentaService;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerPorId(Integer idVenta) {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));
    }

    @Override
    public Venta registrarVenta(Venta venta) {

        // Puedes aplicar lógica extra antes de guardar
        // Ejemplo: asegurarte que total no sea negativo, que cliente exista, etc.
        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizarVenta(Integer idVenta, Venta venta) {
        Venta existente = obtenerPorId(idVenta);

        existente.setCliente(venta.getCliente());
        existente.setVendedor(venta.getVendedor());
        existente.setMetodoPago(venta.getMetodoPago());
        existente.setFecha(venta.getFecha());
        existente.setCanal(venta.getCanal());
        existente.setTotal(venta.getTotal());
        existente.setEstado(venta.getEstado());

        return ventaRepository.save(existente);
    }

    @Override
    public void eliminarVenta(Integer idVenta) {
        if (!ventaRepository.existsById(idVenta)) {
            throw new RuntimeException("Venta no encontrada con ID: " + idVenta);
        }
        ventaRepository.deleteById(idVenta);
    }

}
