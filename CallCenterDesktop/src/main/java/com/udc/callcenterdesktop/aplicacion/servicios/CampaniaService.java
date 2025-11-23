/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;


import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService; // ⬅️ Puerto de Entrada
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository; // ⬅️ Puerto de Salida (dependencia)

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public class CampaniaService implements ICampaniaService { // ⬅️ ¡IMPLEMENTACIÓN CLAVE!
    
    
    private final ICampaniaRepository campaniaRepository;

    /**
     * Constructor para Inyección de Dependencia.
     * @param campaniaRepository El adaptador de persistencia (implementación del puerto).
     */
    public CampaniaService(ICampaniaRepository campaniaRepository) {
        this.campaniaRepository = campaniaRepository;
    }

    @Override
    public Campania crearOActualizarCampania(Campania campania) {
        // [Regla de Negocio 1]: Validar fechas
        if (campania.getFechaInicio().isAfter(campania.getFechaFin())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
        // Llamada al puerto de salida
        return campaniaRepository.guardar(campania);
    }

    @Override
    public Optional<Campania> obtenerCampaniaPorId(int id) {
        return campaniaRepository.buscarPorId(id);
    }

    @Override
    public List<Campania> obtenerTodasCampanias() {
        return campaniaRepository.buscarTodas();
    }

    @Override
    public boolean eliminarCampania(int id) {
        // [Regla de Negocio 2]: Podría incluir una verificación antes de eliminar.
        return campaniaRepository.eliminarPorId(id);
    }

    @Override
    public List<Campania> obtenerCampaniasActivas(LocalDate fechaActual) {
        // Lógica de filtrado de negocio
        return campaniaRepository.buscarTodas().stream()
                .filter(c -> !c.getFechaInicio().isAfter(fechaActual) && 
                             !c.getFechaFin().isBefore(fechaActual))
                .toList();
    }
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

