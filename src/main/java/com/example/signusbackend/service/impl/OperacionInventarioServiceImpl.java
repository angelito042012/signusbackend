package com.example.signusbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.OperacionInventario;
import com.example.signusbackend.repository.OperacionInventarioRepository;
import com.example.signusbackend.service.OperacionInventarioService;

@Service
public class OperacionInventarioServiceImpl implements OperacionInventarioService{

    private final OperacionInventarioRepository repo;

    public OperacionInventarioServiceImpl(OperacionInventarioRepository repo) {
        this.repo = repo;
    }

    @Override
    public OperacionInventario crearOperacionInventario(OperacionInventario operacionInventario) {
        return repo.save(operacionInventario);
    }

    @Override
    public List<OperacionInventario> listarOperacionesInventario() {
        return repo.findAll();
    }

    @Override
    public Optional<OperacionInventario> buscarOperacionPorId(Integer idOperacion) {
        return repo.findById(idOperacion);
    }

    @Override
    public void eliminarOperacionInventario(Integer idOperacion) {
        repo.deleteById(idOperacion);
    }

    @Override
    public OperacionInventario actualizarOperacionInventario(Integer idOperacion,
            OperacionInventario nuevaOperacion) {
        return repo.findById(idOperacion)
                .map(op -> {
                    op.setTipoOperacion(nuevaOperacion.getTipoOperacion());
                    op.setFecha(nuevaOperacion.getFecha());
                    op.setMotivo(nuevaOperacion.getMotivo());
                    op.setEncargado(nuevaOperacion.getEncargado());
                    return repo.save(op);
                })
                .orElseThrow(() -> new RuntimeException("La operación de inventario no fue encontrada"));
    }
    
}
