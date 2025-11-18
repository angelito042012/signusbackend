package com.example.signusbackend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_vendedor", referencedColumnName = "id_empleado", nullable = true)
    private Empleado vendedor;
    // NOTA: nullable = true 
    // porque ventas online normalmente NO tienen vendedor

    @ManyToOne
    @JoinColumn(name = "id_metodopago", referencedColumnName = "id_metodopago", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "canal", nullable = false)
    private String canal; 
    // Ejemplos: "ONLINE", "PRESENCIAL"

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "estado", nullable = false)
    private String estado;
    // Ejemplos: "PAGADO", "PENDIENTE", "CANCELADO"

}
