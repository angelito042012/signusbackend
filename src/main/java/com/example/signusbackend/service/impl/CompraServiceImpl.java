package com.example.signusbackend.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.entity.Inventario;
import com.example.signusbackend.entity.MetodoPago;
import com.example.signusbackend.entity.MovimientoInventario;
import com.example.signusbackend.entity.Pedido;
import com.example.signusbackend.entity.Producto;
import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.entity.dto.CompraOnlineRequestDTO;
import com.example.signusbackend.entity.dto.ProductoCompraDTO;
import com.example.signusbackend.repository.DetalleVentaRepository;
import com.example.signusbackend.repository.MetodoPagoRepository;
import com.example.signusbackend.repository.MovimientoInventarioRepository;
import com.example.signusbackend.repository.PedidoRepository;
import com.example.signusbackend.repository.VentaRepository;
import com.example.signusbackend.service.ClienteService;
import com.example.signusbackend.service.DetalleVentaService;
import com.example.signusbackend.service.InventarioService;
import com.example.signusbackend.service.MetodoPagoService;
import com.example.signusbackend.service.MovimientoInventarioService;
import com.example.signusbackend.service.PedidoService;
import com.example.signusbackend.service.ProductoService;
import com.example.signusbackend.service.VentaService;

import jakarta.transaction.Transactional;

//Este servicio no tiene interface ni entidad

@Service
public class CompraServiceImpl {
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final VentaService ventaService;
    private final DetalleVentaService detalleVentaService;
    private final PedidoService pedidoService;
    private final MovimientoInventarioService movimientoInventarioService;
    private final MetodoPagoService metodoPagoService;
    private final InventarioService inventarioService;

    public CompraServiceImpl(ProductoService productoService, ClienteService clienteService,
            VentaService ventaService, DetalleVentaService detalleVentaService,
            PedidoService pedidoService, MovimientoInventarioService movimientoInventarioService,
            MetodoPagoService metodoPagoService, InventarioService inventarioService) {
        this.productoService = productoService;
        this.clienteService = clienteService;
        this.ventaService = ventaService;
        this.detalleVentaService = detalleVentaService;
        this.pedidoService = pedidoService;
        this.movimientoInventarioService = movimientoInventarioService;
        this.metodoPagoService = metodoPagoService;
        this.inventarioService = inventarioService;
    }

    @Transactional
    public Pedido procesarCompraOnline(CompraOnlineRequestDTO request) {
        // 1. Validar cliente
        Cliente cliente = clienteService.buscarClientePorId(request.getIdCliente());

        // 2. Obtener el método de pago por su nombre
        MetodoPago metodoPago = metodoPagoService.obtenerPorNombre(request.getMetodoPago());

        // 3. Crear la venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setMetodoPago(metodoPago);
        venta.setFecha(LocalDateTime.now());
        venta.setCanal("ONLINE");
        venta.setEstado("PAGADO");

        double total = 0;

        // 4. Registrar los detalles de la venta y calcular el total
        for (ProductoCompraDTO productoCompra : request.getProductos()) {
            // Obtener el inventario del producto
            Inventario inventario = inventarioService.obtenerPorIdProducto(productoCompra.getIdProducto());
            Producto producto = inventario.getProducto();

            if (inventario.getStockActual() < productoCompra.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            // Calcular el subtotal para este producto
            total += producto.getPrecio() * productoCompra.getCantidad();
        }

        // Asignar el total calculado a la venta
        venta.setTotal(total);

        // Guardar la venta
        venta = ventaService.registrarVenta(venta);

        // 5. Registrar los detalles de la venta y actualizar inventario
        for (ProductoCompraDTO productoCompra : request.getProductos()) {
            Inventario inventario = inventarioService.obtenerPorIdProducto(productoCompra.getIdProducto());
            Producto producto = inventario.getProducto();

            // Crear detalle de venta
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(productoCompra.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalleVentaService.agregarDetalle(detalle);

            // Actualizar stock en el inventario
            int stockAnterior = inventario.getStockActual();
            inventario.setStockActual(stockAnterior - productoCompra.getCantidad());
            inventarioService.actualizarInventario(inventario.getIdInventario(), inventario);

            // Registrar movimiento de inventario
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setProducto(producto);
            movimiento.setVenta(venta);
            movimiento.setTipoMovimiento("SALIDA");
            movimiento.setCantidad(productoCompra.getCantidad());
            movimiento.setStockAnterior(stockAnterior);
            movimiento.setStockNuevo(inventario.getStockActual());
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimientoInventarioService.crearMovimiento(movimiento);
        }

        // 6. Crear el pedido
        Pedido pedido = pedidoService.crearPedidoDesdeVenta(
            venta,
            request.getDireccionEnvio(),
            request.getTipoEnvio()
        );

        return pedido;
    }
}
