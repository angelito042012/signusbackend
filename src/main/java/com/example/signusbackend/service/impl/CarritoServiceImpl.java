package com.example.signusbackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Carrito;
import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.repository.CarritoRepository;
import com.example.signusbackend.repository.ClienteRepository;
import com.example.signusbackend.service.CarritoService;

@Service
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ClienteRepository clienteRepository;

    public CarritoServiceImpl(CarritoRepository carritoRepository,
                              ClienteRepository clienteRepository) {
        this.carritoRepository = carritoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Carrito> listarCarritos() {
        
        return carritoRepository.findAll();
    }

    @Override
    public Carrito crearCarrito(Carrito carrito) {

        // Evitar crear más de un carrito por cliente
        if (carritoRepository.existsByCliente(carrito.getCliente())) {
            throw new RuntimeException("El cliente ya tiene un carrito creado.");
        }

        // Establecer la fecha de modificación al momento de la creación
        carrito.setFechaModificacion(LocalDateTime.now());

        // Guardar y devolver el carrito creado
        return carritoRepository.save(carrito);

    }

    @Override
    public Carrito obtenerCarritoPorCliente(Integer idCliente) {

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + idCliente));

        return carritoRepository.findByCliente(cliente)
                .orElseThrow(() -> new RuntimeException("El cliente no tiene un carrito creado."));
    }

    @Override
    public Carrito actualizarFechaModificacion(Integer idCarrito) {

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado con ID: " + idCarrito));

        carrito.setFechaModificacion(LocalDateTime.now());

        return carritoRepository.save(carrito);
    }

    @Override
    public void eliminarCarrito(Integer idCarrito) {

        if (!carritoRepository.existsById(idCarrito)) {
            throw new RuntimeException("Carrito no encontrado con ID: " + idCarrito);
        }

        carritoRepository.deleteById(idCarrito);
    }
    
}
