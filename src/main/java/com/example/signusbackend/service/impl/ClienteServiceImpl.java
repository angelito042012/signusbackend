package com.example.signusbackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Carrito;
import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.repository.ClienteRepository;
import com.example.signusbackend.service.CarritoService;
import com.example.signusbackend.service.ClienteService;

import jakarta.transaction.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final CarritoService carritoService;

    public ClienteServiceImpl(ClienteRepository clienteRepository, CarritoService carritoService) {
        this.clienteRepository = clienteRepository;
        this.carritoService = carritoService;
    }

    @Override
    public List<Cliente> listarTodosLosClientes() {
        // Retorna todos los clientes de la base de datos
        return clienteRepository.findAll();
    }

    @Override
    @Transactional
    public Cliente registrarCliente(Cliente cliente) {
        // 1. Guardar cliente
        Cliente guardado = clienteRepository.save(cliente);

        // 2. Crear carrito vacío
        Carrito carrito = new Carrito();
        carrito.setCliente(guardado);
        carrito.setFechaModificacion(LocalDateTime.now());
        carrito.setEstado("ACTIVO");

        carritoService.crearCarrito(carrito);

        return guardado;
    }

    @Override
    public Cliente actualizarDatosCliente(Integer id, Cliente cliente) {
        // Busca el cliente existente por ID
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Actualiza los campos del cliente existente
        clienteExistente.setNombres(cliente.getNombres());
        clienteExistente.setApellidos(cliente.getApellidos());
        clienteExistente.setDni(cliente.getDni());
        clienteExistente.setTelefono(cliente.getTelefono());
        clienteExistente.setDireccion(cliente.getDireccion());
        clienteExistente.setDepartamento(cliente.getDepartamento());
        clienteExistente.setProvincia(cliente.getProvincia());
        clienteExistente.setDistrito(cliente.getDistrito());

        // Guarda los cambios en la base de datos
        return clienteRepository.save(clienteExistente);
    }

    @Override
    public void eliminarCliente(Integer id) {
        // Verifica si el cliente existe antes de eliminarlo
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }

        // Elimina el cliente de la base de datos
        clienteRepository.deleteById(id);
    }
    
}
