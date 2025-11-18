package com.example.signusbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Carrito;
import com.example.signusbackend.entity.CarritoDetalle;
import com.example.signusbackend.entity.Producto;
import com.example.signusbackend.repository.CarritoDetalleRepository;
import com.example.signusbackend.repository.CarritoRepository;
import com.example.signusbackend.repository.ProductoRepository;
import com.example.signusbackend.service.CarritoDetalleService;

@Service
public class CarritoDetalleServiceImpl implements CarritoDetalleService {

    private final CarritoDetalleRepository detalleRepository;
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    public CarritoDetalleServiceImpl(CarritoDetalleRepository detalleRepository,
                                     CarritoRepository carritoRepository,
                                     ProductoRepository productoRepository) {

        this.detalleRepository = detalleRepository;
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<CarritoDetalle> listarDetallesPorCarrito(Integer idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        return detalleRepository.findByCarrito(carrito);
    }

    @Override
    public CarritoDetalle agregarProductoAlCarrito(Integer idCarrito, Integer idProducto, Integer cantidad) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Si ya existe, solo aumenta cantidad
        if (detalleRepository.existsByCarritoAndProducto(carrito, producto)) {
            CarritoDetalle existente =
                    detalleRepository.findByCarritoAndProducto(carrito, producto).get();

            existente.setCantidad(existente.getCantidad() + cantidad);
            existente.setSubtotal(existente.getCantidad() * existente.getPrecioUnitario());
            return detalleRepository.save(existente);
        }

        // Crear nuevo detalle
        CarritoDetalle nuevo = new CarritoDetalle();
        nuevo.setCarrito(carrito);
        nuevo.setProducto(producto);
        nuevo.setCantidad(cantidad);
        nuevo.setPrecioUnitario(producto.getPrecio());
        nuevo.setSubtotal(producto.getPrecio() * cantidad);

        return detalleRepository.save(nuevo);
    }

    @Override
    public CarritoDetalle actualizarCantidad(Integer idDetalle, Integer cantidad) {
        CarritoDetalle detalle = detalleRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        detalle.setCantidad(cantidad);
        detalle.setSubtotal(detalle.getPrecioUnitario() * cantidad);

        return detalleRepository.save(detalle);
    }

    @Override
    public void eliminarDetalle(Integer idDetalle) {
        if (!detalleRepository.existsById(idDetalle)) {
            throw new RuntimeException("Detalle no encontrado");
        }

        detalleRepository.deleteById(idDetalle);
    }
    
}
