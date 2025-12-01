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
import com.example.signusbackend.service.impl.OperacionInventarioServiceImpl;

public class OperacionInventarioServiceImplTest {
    @Mock
    private OperacionInventarioRepository operacionRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @Mock
    private DetalleOperacionInventarioRepository detalleRepository;

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MovimientoInventarioRepository movimientoRepository;

    @InjectMocks
    private OperacionInventarioServiceImpl operacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarOperacionConDetalles_exito() {
        // Arrange
        OperacionInventarioRequestDTO dto = new OperacionInventarioRequestDTO();
        dto.setIdEncargado(1);
        dto.setTipoOperacion("ENTRADA");
        dto.setMotivo("Ingreso de productos");
        dto.setDetalles(List.of(new DetalleOperacionDTO(1, 10)));

        Empleado encargado = new Empleado();
        encargado.setIdEmpleado(1);

        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombre("Producto 1");

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setStockActual(50);

        OperacionInventario operacionGuardada = new OperacionInventario();
        operacionGuardada.setIdOperacion(1);

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(inventarioRepository.findByProducto_IdProducto(1)).thenReturn(Optional.of(inventario));
        when(operacionRepository.save(any(OperacionInventario.class))).thenReturn(operacionGuardada);

        // Act
        OperacionInventario result = operacionService.registrarOperacionConDetalles(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getIdOperacion());
        verify(operacionRepository, times(1)).save(any(OperacionInventario.class));
        verify(detalleRepository, times(1)).save(any(DetalleOperacionInventario.class));
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
        verify(movimientoRepository, times(1)).save(any(MovimientoInventario.class));
    }

    @Test
    void registrarOperacionConDetalles_encargadoNoEncontrado() {
        // Arrange
        OperacionInventarioRequestDTO dto = new OperacionInventarioRequestDTO();
        dto.setIdEncargado(99); // Encargado inexistente

        when(empleadoRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            operacionService.registrarOperacionConDetalles(dto);
        });

        assertEquals("Encargado no encontrado", exception.getMessage());
        verify(empleadoRepository, times(1)).findById(99);
    }

    @Test
    void registrarOperacionConDetalles_productoNoEncontrado() {
        // Arrange
        OperacionInventarioRequestDTO dto = new OperacionInventarioRequestDTO();
        dto.setIdEncargado(1);
        dto.setTipoOperacion("ENTRADA");
        dto.setDetalles(List.of(new DetalleOperacionDTO(99, 10))); // Producto inexistente

        Empleado encargado = new Empleado();
        encargado.setIdEmpleado(1);

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            operacionService.registrarOperacionConDetalles(dto);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, times(1)).findById(99);
    }

    @Test
    void registrarOperacionConDetalles_stockInsuficiente() {
        // Arrange
        OperacionInventarioRequestDTO dto = new OperacionInventarioRequestDTO();
        dto.setIdEncargado(1);
        dto.setTipoOperacion("SALIDA");
        dto.setDetalles(List.of(new DetalleOperacionDTO(1, 100))); // Cantidad mayor al stock

        Empleado encargado = new Empleado();
        encargado.setIdEmpleado(1);

        Producto producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombre("Producto 1");

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setStockActual(50); // Stock insuficiente

        when(empleadoRepository.findById(1)).thenReturn(Optional.of(encargado));
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(inventarioRepository.findByProducto_IdProducto(1)).thenReturn(Optional.of(inventario));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            operacionService.registrarOperacionConDetalles(dto);
        });

        assertEquals("Stock insuficiente", exception.getMessage());
        verify(inventarioRepository, times(1)).findByProducto_IdProducto(1);
    }

}
