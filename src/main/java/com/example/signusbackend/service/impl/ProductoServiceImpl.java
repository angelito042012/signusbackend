package com.example.signusbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Inventario;
import com.example.signusbackend.entity.Producto;
import com.example.signusbackend.repository.InventarioRepository;
import com.example.signusbackend.repository.ProductoRepository;
import com.example.signusbackend.service.ProductoService;

import jakarta.transaction.Transactional;

@Service
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, InventarioRepository inventarioRepository) {
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto obtenerProducto(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    

    @Override
    public Producto actualizarProducto(Integer id, Producto producto) {
        Producto existente = obtenerProducto(id);

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setImagen(producto.getImagen());
        existente.setEstado(producto.getEstado());
        existente.setCategoria(producto.getCategoria());

        return productoRepository.save(existente);
    }

    @Override
    public void eliminarProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> listarPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoria_IdCategoria(idCategoria);
    }

    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {

        // Verificar si ya existe un inventario para el producto
        if (inventarioRepository.existsByProducto_IdProducto(producto.getIdProducto())) {
            throw new RuntimeException("El inventario de este producto ya existe.");
        }

        // 1. Crear el producto
        Producto nuevo = productoRepository.save(producto);

        // 2. Crear inventario inicial
        Inventario inventario = new Inventario();
        inventario.setProducto(nuevo);
        inventario.setStockActual(0);
        inventario.setStockMinimo(0);
        inventario.setStockMaximo(0);
        inventario.setUbicacion(null); // o valor por defecto

        inventarioRepository.save(inventario);

        return nuevo;
    }
}
