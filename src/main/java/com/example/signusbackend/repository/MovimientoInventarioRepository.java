package com.example.signusbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    // Buscar por operación
    List<MovimientoInventario> findByOperacionIdOperacion(Integer idOperacion);

    // Buscar por producto
    List<MovimientoInventario> findByProductoIdProducto(Integer idProducto);

    // Buscar por encargado
    List<MovimientoInventario> findByEncargadoIdEmpleado(Integer idEmpleado);
}
