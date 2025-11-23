package com.example.signusbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.DetalleOperacionInventario;
import com.example.signusbackend.repository.DetalleOperacionInventarioRepository;
import com.example.signusbackend.service.DetalleOperacionInventarioService;

@Service
public class DetalleOperacionInventarioServiceImpl implements DetalleOperacionInventarioService{

    private final DetalleOperacionInventarioRepository repo;

    public DetalleOperacionInventarioServiceImpl(DetalleOperacionInventarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public DetalleOperacionInventario crearDetalle(DetalleOperacionInventario detalle) {
        return repo.save(detalle);
    }

    @Override
    public List<DetalleOperacionInventario> listarDetalles() {
        return repo.findAll();
    }

    @Override
    public Optional<DetalleOperacionInventario> buscarPorId(Integer idDetalle) {
        return repo.findById(idDetalle);
    }

    @Override
    public List<DetalleOperacionInventario> listarPorOperacion(Integer idOperacion) {
        return repo.findByOperacionIdOperacion(idOperacion);
    }

    @Override
    public DetalleOperacionInventario actualizarDetalle(Integer idDetalle, DetalleOperacionInventario detalleNuevo) {
        return repo.findById(idDetalle)
                .map(detalle -> {
                    detalle.setOperacion(detalleNuevo.getOperacion());
                    detalle.setProducto(detalleNuevo.getProducto());
                    detalle.setCantidad(detalleNuevo.getCantidad());
                    return repo.save(detalle);
                })
                .orElseThrow(() -> new RuntimeException("El detalle de operación no fue encontrado"));
    }

    @Override
    public void eliminarDetalle(Integer idDetalle) {
        repo.deleteById(idDetalle);
    }
    
}
