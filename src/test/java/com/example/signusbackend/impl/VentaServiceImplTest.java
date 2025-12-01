package com.example.signusbackend.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.example.signusbackend.service.impl.VentaServiceImpl;

public class VentaServiceImplTest {
    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private MovimientoInventarioRepository movimientoRepository;

    @Mock
    private MetodoPagoRepository metodoPagoRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarVentaFisica_exito() {
        // Arrange
        VentaRegistrarDTO dto = new VentaRegistrarDTO();
        dto.setIdCliente(1);
        dto.setIdVendedor(2);
        dto.setIdMetodoPago(3);
        dto.setCanal("FISICO");
        dto.setTotal(100.0);
        dto.setDetalles(List.of(new DetalleVentaDTO(1, 2, 50.0)));

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);

        Empleado vendedor = new Empleado();
        vendedor.setIdEmpleado(2);

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(3);

        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombre("Producto 1");

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setStockActual(10);

        Venta ventaGuardada = new Venta();
        ventaGuardada.setIdVenta(1);

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(empleadoRepository.findById(2)).thenReturn(Optional.of(vendedor));
        when(metodoPagoRepository.findById(3)).thenReturn(Optional.of(metodoPago));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(inventarioRepository.findByProducto_IdProducto(1)).thenReturn(Optional.of(inventario));
        when(ventaRepository.save(any(Venta.class))).thenReturn(ventaGuardada);

        // Act
        Venta result = ventaService.registrarVentaFisica(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getIdVenta());
        verify(ventaRepository, times(1)).save(any(Venta.class));
        verify(detalleVentaRepository, times(1)).save(any(DetalleVenta.class));
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
        verify(movimientoRepository, times(1)).save(any(MovimientoInventario.class));
    }

    @Test
    void registrarVentaFisica_clienteNoEncontrado() {
        // Arrange
        VentaRegistrarDTO dto = new VentaRegistrarDTO();
        dto.setIdCliente(99); // Cliente inexistente
        dto.setIdVendedor(2);
        dto.setIdMetodoPago(3);

        when(clienteRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ventaService.registrarVentaFisica(dto);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(clienteRepository, times(1)).findById(99);
    }

    @Test
    void registrarVentaFisica_stockInsuficiente() {
        // Arrange
        VentaRegistrarDTO dto = new VentaRegistrarDTO();
        dto.setIdCliente(1);
        dto.setIdVendedor(2);
        dto.setIdMetodoPago(3);
        dto.setDetalles(List.of(new DetalleVentaDTO(1, 20, 50.0))); // Cantidad mayor al stock

        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);

        Empleado vendedor = new Empleado();
        vendedor.setIdEmpleado(2);

        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setIdMetodoPago(3);

        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombre("Producto 1");

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setStockActual(10); // Stock insuficiente

        when(clienteRepository.findById(1)).thenReturn(Optional.of(cliente));
        when(empleadoRepository.findById(2)).thenReturn(Optional.of(vendedor));
        when(metodoPagoRepository.findById(3)).thenReturn(Optional.of(metodoPago));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(inventarioRepository.findByProducto_IdProducto(1)).thenReturn(Optional.of(inventario));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ventaService.registrarVentaFisica(dto);
        });

        assertEquals("Stock insuficiente del producto: Producto 1", exception.getMessage());
        verify(inventarioRepository, times(1)).findByProducto_IdProducto(1);
    }
}
