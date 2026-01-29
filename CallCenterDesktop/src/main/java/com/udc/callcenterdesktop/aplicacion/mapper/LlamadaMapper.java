package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;

public class LlamadaMapper {

    private LlamadaMapper() {
        throw new UnsupportedOperationException("Clase utilitaria.");
    }

    public static Llamada toEntity(LlamadaDTO dto) {
        if (dto == null) return null;

        Llamada llamada = new Llamada();
        llamada.setIdLlamada(dto.getIdLlamada());
        llamada.setFechaHora(dto.getFechaHora());
        llamada.setDuracion(dto.getDuracion());
        llamada.setDetalleResultado(dto.getDetalleResultado());
        llamada.setObservaciones(dto.getObservaciones());
        
        // Mapeo de IDs para la DB
        llamada.setIdAgente(dto.getIdAgente());
        llamada.setIdCliente(dto.getIdCliente());
        llamada.setIdCampania(dto.getIdCampania());

        return llamada;
    }

    public static LlamadaDTO toDTO(Llamada entity) {
        if (entity == null) return null;

        LlamadaDTO dto = new LlamadaDTO();
        dto.setIdLlamada(entity.getIdLlamada());
        dto.setFechaHora(entity.getFechaHora());
        dto.setDuracion(entity.getDuracion());
        dto.setDetalleResultado(entity.getDetalleResultado());
        dto.setObservaciones(entity.getObservaciones());
        
        dto.setIdAgente(entity.getIdAgente());
        dto.setIdCliente(entity.getIdCliente());
        dto.setIdCampania(entity.getIdCampania());

        // Esto es lo que causaba el error si no tenías los objetos en la entidad:
        if (entity.getAgente() != null) {
            dto.setNombreAgente(entity.getAgente().getNombreCompleto());
        }
        if (entity.getCliente() != null) {
            dto.setNombreCliente(entity.getCliente().getNombreCompleto());
        }
        if (entity.getCampania() != null) {
            // Revisa si en Campania es getNombreCampania() o getNombre()
            dto.setNombreCampania(entity.getCampania().getNombreCampania());
        }

        return dto;
    }
}