package com.example.signusbackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.entity.Pedido;
import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.repository.PedidoRepository;
import com.example.signusbackend.service.ClienteService;
import com.example.signusbackend.service.DetalleVentaService;
import com.example.signusbackend.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final DetalleVentaService detalleVentaService;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteService clienteService, DetalleVentaService detalleVentaService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.detalleVentaService = detalleVentaService;
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
        pedido.setFecha(LocalDateTime.now());
        pedido.setDireccionEnvio(direccionEnvio);
        pedido.setTipoEnvio(tipoEnvio);
        pedido.setEstado("EN PROCESO"); // Estado inicial
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

    @Override
    public List<Pedido> listarPedidosPorEmailCliente(String email) {
        // Usar el servicio de Cliente para buscar al cliente por su email
        Cliente cliente = clienteService.findByUsuarioClienteEmail(email);

        // Buscar los pedidos asociados al cliente
        return pedidoRepository.findByVenta_Cliente_IdCliente(cliente.getIdCliente());
    }

    @Override
    public List<DetalleVenta> obtenerDetallesDeVentaDePedido(Integer idPedido) {
        // Buscar el pedido por su ID
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + idPedido));

        // Obtener la venta asociada al pedido
        Venta venta = pedido.getVenta();
        if (venta == null) {
            throw new RuntimeException("El pedido no tiene una venta asociada");
        }

        // Buscar los detalles de la venta
        return detalleVentaService.listarPorVenta(venta.getIdVenta());
    }

}
