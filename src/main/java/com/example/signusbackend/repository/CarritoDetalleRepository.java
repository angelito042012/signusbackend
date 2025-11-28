package com.example.signusbackend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.signusbackend.entity.Carrito;
import com.example.signusbackend.entity.CarritoDetalle;
import com.example.signusbackend.entity.Producto;

public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer>{
    
    List<CarritoDetalle> findByCarrito(Carrito carrito);

    boolean existsByCarritoAndProducto(Carrito carrito, Producto producto);

    Optional<CarritoDetalle> findByCarritoAndProducto(Carrito carrito, Producto producto);

    void deleteByCarrito(Carrito carrito);
}
