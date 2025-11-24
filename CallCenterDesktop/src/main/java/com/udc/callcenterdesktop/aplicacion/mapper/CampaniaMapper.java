/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Campania;

/**
 * Clase utilitaria para la conversión de datos (Mapping) del módulo de Campañas.
 */
public class CampaniaMapper {

    // De DTO (Vista) a Entidad (Dominio)
    public static Campania toEntity(CampaniaDTO dto) {
        if (dto == null) return null;
        return new Campania(
            dto.idCampania,
            dto.nombreCampania,
            dto.tipoCampania,
            dto.fechaInicio,
            dto.fechaFin,
            dto.supervisoresCargo,
            dto.descripcionObjetivos
        );
    }

    // De Entidad (Dominio) a DTO (Vista)
    public static CampaniaDTO toDTO(Campania entity) {
        if (entity == null) return null;
        return new CampaniaDTO(
            entity.getIdCampania(),
            entity.getNombreCampania(),
            entity.getTipoCampania(),
            entity.getFechaInicio(),
            entity.getFechaFin(),
            entity.getSupervisoresCargo(),
            entity.getDescripcionObjetivos()
        );
    }
}