package com.example.signusbackend.service;

import java.util.List;
import java.util.Optional;

import com.example.signusbackend.entity.Cliente;

public interface ClienteService {
    List<Cliente> listarTodosLosClientes();
    Cliente buscarClientePorId(Integer id);
    Cliente findByUsuarioClienteEmail(String email);
    Cliente registrarCliente(Cliente cliente);
    Cliente actualizarDatosCliente(Integer id, Cliente cliente);
    void eliminarCliente(Integer id);
}
