package com.example.signusbackend.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.signusbackend.entity.MetodoPago;
import com.example.signusbackend.repository.MetodoPagoRepository;
import com.example.signusbackend.service.MetodoPagoService;

@Service
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;

    public MetodoPagoServiceImpl(MetodoPagoRepository metodoPagoRepository) {
        this.metodoPagoRepository = metodoPagoRepository;
    }

    @Override
    public List<MetodoPago> listarMetodos() {
        return metodoPagoRepository.findAll();
    }

    @Override
    public MetodoPago obtenerPorId(Integer idMetodo) {
        return metodoPagoRepository.findById(idMetodo)
            .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con id: " + idMetodo));
    }

    @Override
    public MetodoPago crearMetodo(MetodoPago metodoPago) {
        if (metodoPagoRepository.existsByNombre(metodoPago.getNombre())) {
            throw new RuntimeException("El método de pago ya existe: " + metodoPago.getNombre());
        }
        return metodoPagoRepository.save(metodoPago);
    }

    @Override
    public MetodoPago actualizarMetodo(Integer idMetodo, MetodoPago metodoPago) {
        MetodoPago existente = obtenerPorId(idMetodo);

        existente.setNombre(metodoPago.getNombre());
        existente.setDescripcion(metodoPago.getDescripcion());

        return metodoPagoRepository.save(existente);
    }

    @Override
    public void eliminarMetodo(Integer idMetodo) {
        if (!metodoPagoRepository.existsById(idMetodo)) {
            throw new RuntimeException("Método de pago no encontrado con id: " + idMetodo);
        }
        metodoPagoRepository.deleteById(idMetodo);
    }

}
