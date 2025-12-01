package com.example.signusbackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.Cliente;
import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.entity.Inventario;
import com.example.signusbackend.entity.MetodoPago;
import com.example.signusbackend.entity.MovimientoInventario;
import com.example.signusbackend.entity.Producto;
import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.entity.dto.DetalleVentaDTO;
import com.example.signusbackend.entity.dto.VentaRegistrarDTO;
import com.example.signusbackend.repository.ClienteRepository;
import com.example.signusbackend.repository.DetalleVentaRepository;
import com.example.signusbackend.repository.EmpleadoRepository;
import com.example.signusbackend.repository.InventarioRepository;
import com.example.signusbackend.repository.MetodoPagoRepository;
import com.example.signusbackend.repository.MovimientoInventarioRepository;
import com.example.signusbackend.repository.ProductoRepository;
import com.example.signusbackend.repository.VentaRepository;
import com.example.signusbackend.service.VentaService;

import jakarta.transaction.Transactional;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    // Repositorios necesarios para el método compuesto registrarVentaFisica
    private final DetalleVentaRepository detalleVentaRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    

    public VentaServiceImpl(VentaRepository ventaRepository,
            DetalleVentaRepository detalleVentaRepository,
            ClienteRepository clienteRepository,
            EmpleadoRepository empleadoRepository,
            ProductoRepository productoRepository,
            InventarioRepository inventarioRepository,
            MovimientoInventarioRepository movimientoRepository,
            MetodoPagoRepository metodoPagoRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.productoRepository = productoRepository;
        this.inventarioRepository = inventarioRepository;
        this.movimientoRepository = movimientoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta obtenerPorId(Integer idVenta) {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + idVenta));
    }

    @Override
    public Venta registrarVenta(Venta venta) {

        // Puedes aplicar lógica extra antes de guardar
        // Ejemplo: asegurarte que total no sea negativo, que cliente exista, etc.
        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizarVenta(Integer idVenta, Venta venta) {
        Venta existente = obtenerPorId(idVenta);

        existente.setCliente(venta.getCliente());
        existente.setVendedor(venta.getVendedor());
        existente.setMetodoPago(venta.getMetodoPago());
        existente.setFecha(venta.getFecha());
        existente.setCanal(venta.getCanal());
        existente.setTotal(venta.getTotal());
        existente.setEstado(venta.getEstado());

        return ventaRepository.save(existente);
    }

    @Override
    public void eliminarVenta(Integer idVenta) {
        if (!ventaRepository.existsById(idVenta)) {
            throw new RuntimeException("Venta no encontrada con ID: " + idVenta);
        }
        ventaRepository.deleteById(idVenta);
    }



    @Override
    @Transactional
    public Venta registrarVentaFisica(VentaRegistrarDTO dto) {

        Cliente cliente = null;



        if (dto.getIdCliente() != null) {
            cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        }

        Empleado vendedor = empleadoRepository.findById(dto.getIdVendedor())
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        MetodoPago metodo = metodoPagoRepository.findById(dto.getIdMetodoPago())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        // 1. Crear Venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setMetodoPago(metodo);
        venta.setFecha(LocalDateTime.now());
        venta.setCanal(dto.getCanal());
        venta.setTotal(dto.getTotal());
        venta.setEstado("COMPLETADA");

        Venta ventaGuardada = ventaRepository.save(venta); // Guardar la venta y obtener el objeto persistido


        // 2. Procesar cada detalle
        for (DetalleVentaDTO d : dto.getDetalles()) {

            Producto producto = productoRepository.findById(d.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Inventario inv = inventarioRepository.findByProducto_IdProducto(producto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado para el producto: " + producto.getNombre()));

            if (inv.getStockActual() < d.getCantidad()) {
                throw new RuntimeException("Stock insuficiente del producto: " + producto.getNombre());
            }

            // 2.1 Crear detalle de venta
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(ventaGuardada);
            detalle.setProducto(producto);
            detalle.setCantidad(d.getCantidad());
            detalle.setPrecioUnitario(d.getPrecioUnitario());
            detalleVentaRepository.save(detalle);

            // 2.2 Actualizar Inventario
            int stockAnterior = inv.getStockActual();
            int stockNuevo = stockAnterior - d.getCantidad();

            inv.setStockActual(stockNuevo);
            inventarioRepository.save(inv);

            // 2.3 Registrar Movimiento de Inventario (tipo = SALIDA)
            MovimientoInventario mov = new MovimientoInventario();
            mov.setEncargado(vendedor); // El vendedor está ejecutando la salida
            mov.setProducto(producto);
            mov.setOperacion(null); // no se relaciona con operaciones manuales
            mov.setTipoMovimiento("SALIDA");
            mov.setCantidad(d.getCantidad());
            mov.setStockAnterior(stockAnterior);
            mov.setStockNuevo(stockNuevo);
            mov.setFechaMovimiento(LocalDateTime.now());
            mov.setMotivo("VENTA FISICA ID " + venta.getIdVenta());

            movimientoRepository.save(mov);
        }

        //return "Venta registrada con éxito ID: " + venta.getIdVenta();
         //el servicio esta definido para retornar la venta registrada
        return ventaGuardada; // Asegúrate de devolver el objeto persistido
    }

}
