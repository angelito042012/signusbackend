package com.example.signusbackend.service;

import java.util.List;

import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.entity.Pedido;
import com.example.signusbackend.entity.Venta;

public interface PedidoService {
    List<Pedido> listarPedidos();
    List<Pedido> listarPedidosPorEmailCliente(String email);
    List<Pedido> listarPedidosPorEstado(String estado);
    Pedido obtenerPedidoPorId(Integer idPedido);
    Pedido crearPedidoDesdeVenta(Venta venta, String direccionEnvio, String tipoEnvio);
    Pedido modificarPedido(Integer idPedido, Pedido pedidoActualizado);
    Pedido modificarEstadoPedido(Integer idPedido, String nuevoEstado);
    void eliminarPedido(Integer idPedido);
    List<DetalleVenta> obtenerDetallesDeVentaDePedido(Integer idPedido);
}
