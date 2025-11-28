package com.example.signusbackend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.signusbackend.entity.DetalleVenta;
import com.example.signusbackend.entity.Pedido;
import com.example.signusbackend.entity.Venta;
import com.example.signusbackend.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // Listar todos los pedidos
    @GetMapping
    @Operation(summary = "Listar todos los pedidos", description = "Obtiene una lista de todos los pedidos registrados en el sistema.")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    //Listar pedidos por email del cliente
    @GetMapping("/cliente/email/{email}")
    @Operation(summary = "Listar pedidos por email del cliente", description = "Obtiene una lista de pedidos asociados al email del cliente.")
    public ResponseEntity<List<Pedido>> listarPedidosPorEmailCliente(@PathVariable String email) {
        List<Pedido> pedidos = pedidoService.listarPedidosPorEmailCliente(email);
        return ResponseEntity.ok(pedidos);
    }

    // Listar pedidos por estado
    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar pedidos por estado", description = "Obtiene una lista de pedidos filtrados por su estado. ACTIVO O INACTIVO.")
    public ResponseEntity<List<Pedido>> listarPedidosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorEstado(estado));
    }

    // Buscar un pedido por ID

    @GetMapping("/{idPedido}")
    @Operation(summary = "Obtener un pedido por ID", description = "Obtiene los detalles de un pedido específico utilizando su ID.")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Integer idPedido) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(idPedido));
    }

    //
    //Deberia replantear este endpoint
    //

    // Crear un pedido desde una venta
    @PostMapping("/crear-desde-venta")
    @Operation(summary = "Crear un pedido desde una venta", description = "Crea un nuevo pedido en el sistema basado en una venta existente, con dirección y tipo de envío especificados.")
    public ResponseEntity<Pedido> crearPedidoDesdeVenta(@RequestBody Venta venta,
                                                        @RequestParam String direccion,
                                                        @RequestParam String tipoEnvio) {
        return ResponseEntity.ok(pedidoService.crearPedidoDesdeVenta(venta, direccion, tipoEnvio));
    }



    // Modificar un pedido
    @PutMapping("/{idPedido}")
    @Operation(summary = "Modificar un pedido", description = "Actualiza los detalles de un pedido específico utilizando su ID.")
    public ResponseEntity<Pedido> modificarPedido(@PathVariable Integer idPedido, @RequestBody Pedido pedido) {
        return ResponseEntity.ok(pedidoService.modificarPedido(idPedido, pedido));
    }

    // Modificar el estado de un pedido
    @PatchMapping("/{idPedido}/estado")
    @Operation(summary = "Modificar el estado de un pedido", description = "Actualiza el estado de un pedido específico utilizando su ID.")
    public ResponseEntity<Pedido> modificarEstadoPedido(@PathVariable Integer idPedido, @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.modificarEstadoPedido(idPedido, estado));
    }

    // Eliminar un pedido
    @DeleteMapping("/{idPedido}")
    @Operation(summary = "Eliminar un pedido", description = "Elimina un pedido específico del sistema utilizando su ID.")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer idPedido) {
        pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idPedido}/venta/detalles")
    @Operation(summary = "Obtener detalles de venta de un pedido", description = "Obtiene la lista de detalles de venta asociados a un pedido específico utilizando su ID.")
    public ResponseEntity<List<DetalleVenta>> obtenerDetallesDeVentaDePedido(@PathVariable Integer idPedido) {
        List<DetalleVenta> detalles = pedidoService.obtenerDetallesDeVentaDePedido(idPedido);
        return ResponseEntity.ok(detalles);
    }
}
