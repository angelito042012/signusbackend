package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*") //restringir luego
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // GET: listar todos los clientes
    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Obtiene una lista de todos los clientes registrados en el sistema.")
    public List<Cliente> listarClientes() {
        return clienteService.listarTodosLosClientes();
    }

    // POST: registrar un nuevo cliente
    /*@PostMapping
    public Cliente registrarCliente(@RequestBody Cliente cliente) {
        return clienteService.registrarCliente(cliente);
    }*/

    // PUT: actualizar un cliente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente existente", description = "Actualiza los detalles de un cliente específico utilizando su ID.")
    public Cliente actualizarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        return clienteService.actualizarDatosCliente(id, cliente);
    }

    // DELETE: eliminar cliente
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente específico del sistema utilizando su ID.")
    public void eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
    }

    
}
