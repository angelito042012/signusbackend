package com.example.signusbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.MovimientoInventario;
import com.example.signusbackend.repository.MovimientoInventarioRepository;
import com.example.signusbackend.service.MovimientoInventarioService;

@Service
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {
    
    private final MovimientoInventarioRepository repo;
    
    public MovimientoInventarioServiceImpl(MovimientoInventarioRepository repo) {
        this.repo = repo;
    }
    
    // Implementación de los métodos definidos en la interfaz MovimientoInventarioService
    
    @Override
    public MovimientoInventario crearMovimiento(MovimientoInventario movimiento) {
        return repo.save(movimiento);
    }

    @Override
    public List<MovimientoInventario> listarMovimientos() {
        return repo.findAll();
    }

    @Override
    public Optional<MovimientoInventario> buscarPorId(Integer idMovimiento) {
        return repo.findById(idMovimiento);
    }

    @Override
    public List<MovimientoInventario> listarPorOperacion(Integer idOperacion) {
        return repo.findByOperacionIdOperacion(idOperacion);
    }

    @Override
    public List<MovimientoInventario> listarPorProducto(Integer idProducto) {
        return repo.findByProductoIdProducto(idProducto);
    }

    @Override
    public List<MovimientoInventario> listarPorEncargado(Integer idEmpleado) {
        return repo.findByEncargadoIdEmpleado(idEmpleado);
    }

    @Override
    public MovimientoInventario actualizarMovimiento(Integer idMovimiento, MovimientoInventario nuevoMovimiento) {
        return repo.findById(idMovimiento)
                .map(mov -> {
                    mov.setEncargado(nuevoMovimiento.getEncargado());
                    mov.setProducto(nuevoMovimiento.getProducto());
                    mov.setOperacion(nuevoMovimiento.getOperacion());
                    mov.setTipoMovimiento(nuevoMovimiento.getTipoMovimiento());
                    mov.setCantidad(nuevoMovimiento.getCantidad());
                    mov.setStockAnterior(nuevoMovimiento.getStockAnterior());
                    mov.setStockNuevo(nuevoMovimiento.getStockNuevo());
                    mov.setFechaMovimiento(nuevoMovimiento.getFechaMovimiento());
                    mov.setMotivo(nuevoMovimiento.getMotivo());
                    return repo.save(mov);
                })
                .orElseThrow(() -> new RuntimeException("El movimiento inventario no fue encontrado"));
    }

    @Override
    public void eliminarMovimiento(Integer idMovimiento) {
        repo.deleteById(idMovimiento);
    }
}
