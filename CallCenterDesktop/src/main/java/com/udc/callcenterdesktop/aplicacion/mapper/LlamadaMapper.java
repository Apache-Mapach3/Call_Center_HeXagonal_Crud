/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

/**
 *
 * @author camolano
 */
import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;

public class LlamadaMapper {

    /** Convierte Entidad a DTO (de Dominio a Aplicación/Vista) */
    public LlamadaDTO toDTO(Llamada entity) {
        LlamadaDTO dto = new LlamadaDTO();
        dto.setIdLlamada((long) entity.getIdLlamada());
        dto.setFechaHora(entity.getFechaHora());
        dto.setDuracion(entity.getDuracion());
        dto.setDetalle(entity.getDetalle());
        dto.setObservacion(entity.getObservacion());
        
        // IDs de las relaciones (solo si no se conocen los nombres en la Entidad)
        dto.setIdAgente((long) entity.getIdAgente());
        dto.setIdCampania((long) entity.getIdCampania());
        dto.setIdCliente((long) entity.getIdCliente());
        
        // Los campos 'nombreAgente', 'nombreCampania', 'nombreCliente'
        // deben ser llenados en el LlamadaService (usando INNER JOIN o búsquedas adicionales)
        
        return dto;
    }

    /** Convierte DTO a Entidad (de Aplicación/Vista a Dominio/Persistencia) */
    public Llamada toEntity(LlamadaDTO dto) {
        Llamada entity = new Llamada();
        entity.setIdLlamada(dto.getIdLlamada() != null ? dto.getIdLlamada().intValue() : 0);
        entity.setFechaHora(dto.getFechaHora());
        entity.setDuracion(dto.getDuracion());
        entity.setDetalle(dto.getDetalle());
        entity.setObservacion(dto.getObservacion());

        
        
        return entity;
    }
}