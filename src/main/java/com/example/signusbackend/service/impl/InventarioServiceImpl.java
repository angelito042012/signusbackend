package com.example.signusbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Inventario;
import com.example.signusbackend.repository.InventarioRepository;
import com.example.signusbackend.service.InventarioService;

@Service
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioServiceImpl(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<Inventario> listarInventario() {
        return inventarioRepository.findAll();
    }

    @Override
    public Inventario obtenerPorId(Integer idInventario) {
        return inventarioRepository.findById(idInventario)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado con ID: " + idInventario));
    }

    @Override
    public Inventario obtenerPorIdProducto(Integer idProducto) {
        Inventario inv = inventarioRepository.findByProducto_IdProducto(idProducto);
        if (inv == null) {
            throw new RuntimeException("Inventario no encontrado para el producto ID: " + idProducto);
        }
        return inv;
    }

    @Override
    public Inventario crearInventario(Inventario inventario) {
        if (inventarioRepository.existsByProducto_IdProducto(inventario.getProducto().getIdProducto())) {
            throw new RuntimeException("El producto ya tiene inventario asignado.");
        }
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizarInventario(Integer idInventario, Inventario inventario) {
        Inventario existente = obtenerPorId(idInventario);

        existente.setStockActual(inventario.getStockActual());
        existente.setStockMinimo(inventario.getStockMinimo());
        existente.setStockMaximo(inventario.getStockMaximo());
        existente.setUbicacion(inventario.getUbicacion());

        return inventarioRepository.save(existente);
    }

    @Override
    public void eliminarInventario(Integer idInventario) {
        if (!inventarioRepository.existsById(idInventario)) {
            throw new RuntimeException("Inventario no encontrado con ID: " + idInventario);
        }
        inventarioRepository.deleteById(idInventario);
    }
}
