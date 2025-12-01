package com.example.signusbackend.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.DetalleOperacionInventario;
import com.example.signusbackend.entity.Empleado;
import com.example.signusbackend.entity.Inventario;
import com.example.signusbackend.entity.MovimientoInventario;
import com.example.signusbackend.entity.OperacionInventario;
import com.example.signusbackend.entity.Producto;
import com.example.signusbackend.entity.dto.DetalleOperacionDTO;
import com.example.signusbackend.entity.dto.OperacionInventarioRequestDTO;
import com.example.signusbackend.repository.DetalleOperacionInventarioRepository;
import com.example.signusbackend.repository.EmpleadoRepository;
import com.example.signusbackend.repository.InventarioRepository;
import com.example.signusbackend.repository.MovimientoInventarioRepository;
import com.example.signusbackend.repository.OperacionInventarioRepository;
import com.example.signusbackend.repository.ProductoRepository;
import com.example.signusbackend.service.OperacionInventarioService;

import jakarta.transaction.Transactional;

@Service
public class OperacionInventarioServiceImpl implements OperacionInventarioService {

    private final OperacionInventarioRepository operacionRepository;

    // Repositories necesarios para operaciones complejas
    private final EmpleadoRepository empleadoRepository;
    private final DetalleOperacionInventarioRepository detalleRepository;
    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    public OperacionInventarioServiceImpl(OperacionInventarioRepository operacionRepository,
            EmpleadoRepository empleadoRepository,
            DetalleOperacionInventarioRepository detalleRepository,
            InventarioRepository inventarioRepository,
            ProductoRepository productoRepository,
            MovimientoInventarioRepository movimientoRepository) {
        this.operacionRepository = operacionRepository;
        this.empleadoRepository = empleadoRepository;
        this.detalleRepository = detalleRepository;
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public OperacionInventario crearOperacionInventario(OperacionInventario operacionInventario) {
        return operacionRepository.save(operacionInventario);
    }

    @Override
    public List<OperacionInventario> listarOperacionesInventario() {
        return operacionRepository.findAll();
    }

    @Override
    public Optional<OperacionInventario> buscarOperacionPorId(Integer idOperacion) {
        return operacionRepository.findById(idOperacion);
    }

    @Override
    public void eliminarOperacionInventario(Integer idOperacion) {
        operacionRepository.deleteById(idOperacion);
    }

    @Override
    public OperacionInventario actualizarOperacionInventario(Integer idOperacion,
            OperacionInventario nuevaOperacion) {
        return operacionRepository.findById(idOperacion)
                .map(op -> {
                    op.setTipoOperacion(nuevaOperacion.getTipoOperacion());
                    op.setFecha(nuevaOperacion.getFecha());
                    op.setMotivo(nuevaOperacion.getMotivo());
                    op.setEncargado(nuevaOperacion.getEncargado());
                    return operacionRepository.save(op);
                })
                .orElseThrow(() -> new RuntimeException("La operación de inventario no fue encontrada"));
    }

    @Transactional
    @Override
    public OperacionInventario registrarOperacionConDetalles(OperacionInventarioRequestDTO dto) {

        Empleado encargado = empleadoRepository.findById(dto.getIdEncargado())
                .orElseThrow(() -> new RuntimeException("Encargado no encontrado"));

        // 1. Crear operación
        OperacionInventario operacion = new OperacionInventario();
        operacion.setTipoOperacion(dto.getTipoOperacion());
        operacion.setEncargado(encargado);
        operacion.setFecha(LocalDateTime.now());
        operacion.setMotivo(dto.getMotivo());

        operacion = operacionRepository.save(operacion);

        // 2. Procesar detalles
        for (DetalleOperacionDTO det : dto.getDetalles()) {

            Producto producto = productoRepository.findById(det.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Inventario inventario = inventarioRepository.findByProducto_IdProducto(det.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

            Integer stockAnterior = inventario.getStockActual();
            Integer stockNuevo;

            switch (dto.getTipoOperacion()) {
                case "ENTRADA":
                    stockNuevo = stockAnterior + det.getCantidad();
                    break;
                case "SALIDA":
                    if (stockAnterior < det.getCantidad())
                        throw new RuntimeException("Stock insuficiente");
                    stockNuevo = stockAnterior - det.getCantidad();
                    break;
                case "AJUSTE":
                    stockNuevo = det.getCantidad(); // valor exacto
                    break;
                default:
                    throw new RuntimeException("Tipo de operación inválido");
            }

            // 2.1 Actualizar inventario
            inventario.setStockActual(stockNuevo);
            inventarioRepository.save(inventario);

            // 2.2 Registrar detalle
            DetalleOperacionInventario detalle = new DetalleOperacionInventario();
            detalle.setOperacion(operacion);
            detalle.setProducto(producto);
            detalle.setCantidad(det.getCantidad());
            detalleRepository.save(detalle);

            // 2.3 Registrar movimiento
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setEncargado(encargado);
            movimiento.setProducto(producto);
            movimiento.setOperacion(operacion);
            movimiento.setTipoMovimiento(dto.getTipoOperacion());
            movimiento.setCantidad(det.getCantidad());
            movimiento.setStockAnterior(stockAnterior);
            movimiento.setStockNuevo(stockNuevo);
            movimiento.setFechaMovimiento(LocalDateTime.now());
            movimiento.setMotivo(dto.getMotivo());
            movimientoRepository.save(movimiento);
        }

        // se retorna la operacion registrada, asi esta puesto en el service interface
        return operacion;
    }

}
