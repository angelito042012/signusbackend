package com.example.signusbackend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Pedido;
import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.repository.PedidoRepository;
import com.example.signusbackend.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> listarPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    @Override
    public Pedido crearPedidoDesdeVenta(Venta venta, String direccionEnvio, String tipoEnvio) {
        Pedido pedido = new Pedido();
        pedido.setVenta(venta);
        pedido.setDireccionEnvio(direccionEnvio);
        pedido.setTipoEnvio(tipoEnvio);
        pedido.setEstado("en proceso"); // Estado inicial
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido modificarPedido(Integer idPedido, Pedido pedidoActualizado) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(idPedido);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            pedido.setDireccionEnvio(pedidoActualizado.getDireccionEnvio());
            pedido.setTipoEnvio(pedidoActualizado.getTipoEnvio());
            pedido.setCodigoSeguimiento(pedidoActualizado.getCodigoSeguimiento());
            return pedidoRepository.save(pedido);
        } else {
            throw new RuntimeException("Pedido no encontrado con ID: " + idPedido);
        }
    }

    @Override
    public Pedido modificarEstadoPedido(Integer idPedido, String nuevoEstado) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(idPedido);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            pedido.setEstado(nuevoEstado);
            return pedidoRepository.save(pedido);
        } else {
            throw new RuntimeException("Pedido no encontrado con ID: " + idPedido);
        }
    }

    @Override
    public void eliminarPedido(Integer idPedido) {
        if (pedidoRepository.existsById(idPedido)) {
            pedidoRepository.deleteById(idPedido);
        } else {
            throw new RuntimeException("Pedido no encontrado con ID: " + idPedido);
        }
    }

    @Override
    public Pedido obtenerPedidoPorId(Integer idPedido) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(idPedido);
        if (pedidoExistente.isPresent()) {
            return pedidoExistente.get();
        } else {
            throw new RuntimeException("Pedido no encontrado con ID: " + idPedido);
        }
    }
    
}
