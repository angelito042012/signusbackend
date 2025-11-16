package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.Cliente;

public interface ClienteService {
    List<Cliente> listarTodosLosClientes();
    Cliente registrarCliente(Cliente cliente);
    Cliente actualizarDatosCliente(Integer id, Cliente cliente);
    void eliminarCliente(Integer id);
}
