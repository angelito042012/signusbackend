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

@Entity
@Table(name = "venta")
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

    public Integer getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Integer idVenta) {
        this.idVenta = idVenta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Empleado getVendedor() {
        return vendedor;
    }

    public void setVendedor(Empleado vendedor) {
        this.vendedor = vendedor;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    
}
