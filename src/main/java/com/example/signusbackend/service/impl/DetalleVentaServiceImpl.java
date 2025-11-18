package com.example.signusbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.repository.DetalleVentaRepository;
import com.example.signusbackend.service.DetalleVentaService;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    public DetalleVentaServiceImpl(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    public List<DetalleVenta> listarPorVenta(Integer idVenta) {
        return detalleVentaRepository.findByVentaIdVenta(idVenta);
    }

    @Override
    public DetalleVenta agregarDetalle(DetalleVenta detalle) {
        return detalleVentaRepository.save(detalle);
    }

    @Override
    public void eliminarDetalle(Integer idDetalle) {
        if (!detalleVentaRepository.existsById(idDetalle)) {
            throw new RuntimeException("Detalle no encontrado");
        }
        detalleVentaRepository.deleteById(idDetalle);
    }
}
