package com.example.signusbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.Pedido;
import com.example.signusbackend.entity.dto.CompraOnlineRequestDTO;
import com.example.signusbackend.service.impl.CompraServiceImpl;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "*")
public class CompraController {
    private final CompraServiceImpl compraService;

    public CompraController(CompraServiceImpl compraService) {
        this.compraService = compraService;
    }

    @PostMapping("/online")
    @Operation(summary = "Realizar compra online", description = "Procesa una compra online, creando la venta, los detalles de venta, el pedido y actualizando el inventario.")
    public ResponseEntity<?> realizarCompraOnline(@RequestBody CompraOnlineRequestDTO request) {
        try {
            Pedido pedido = compraService.procesarCompraOnline(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
