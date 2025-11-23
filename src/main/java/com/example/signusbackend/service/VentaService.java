package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.entity.dto.VentaRegistrarDTO;

public interface VentaService {
    
    List<Venta> listarVentas();

    Venta obtenerPorId(Integer idVenta);

    Venta registrarVenta(Venta venta);

    Venta actualizarVenta(Integer idVenta, Venta venta);

    void eliminarVenta(Integer idVenta);

    // Nuevo método compuesto para registrar venta física

    Venta registrarVentaFisica(VentaRegistrarDTO dto);
}
