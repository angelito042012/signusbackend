package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.Pedido;
import com.example.signusbackend.entity.Venta;

public interface PedidoService {
    List<Pedido> listarPedidos();
    List<Pedido> listarPedidosPorEstado(String estado);
    Pedido crearPedidoDesdeVenta(Venta venta, String direccion, String tipoEnvio);
    Pedido modificarPedido(Integer idPedido, Pedido pedidoActualizado);
    Pedido modificarEstadoPedido(Integer idPedido, String nuevoEstado);
    void eliminarPedido(Integer idPedido);
}
