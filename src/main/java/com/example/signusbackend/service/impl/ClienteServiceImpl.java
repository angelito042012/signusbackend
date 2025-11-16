package com.example.signusbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.repository.ClienteRepository;
import com.example.signusbackend.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public List<Cliente> listarTodosLosClientes() {
        // Retorna todos los clientes de la base de datos
        return clienteRepository.findAll();
    }

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        // Guarda un nuevo cliente en la base de datos
        return clienteRepository.save(cliente);
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
        clienteExistente.setDireccion(cliente.getDireccion());
        clienteExistente.setTelefono(cliente.getTelefono());

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
