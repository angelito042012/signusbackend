package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.OperacionInventario;
import com.example.signusbackend.entity.dto.OperacionInventarioRequestDTO;

public interface OperacionInventarioService {
    OperacionInventario crearOperacionInventario(OperacionInventario operacionInventario);
    List<OperacionInventario> listarOperacionesInventario();
    Optional<OperacionInventario> buscarOperacionPorId(Integer idOperacion);
    void eliminarOperacionInventario(Integer idOperacion);
    OperacionInventario actualizarOperacionInventario(Integer idOperacion, OperacionInventario nuevaOperacion);

    OperacionInventario registrarOperacionConDetalles(OperacionInventarioRequestDTO dto);
}
