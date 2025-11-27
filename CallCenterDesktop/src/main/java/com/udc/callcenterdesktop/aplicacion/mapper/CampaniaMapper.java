/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Campania;

/**
 * Mapper para conversiones entre Campaña (Entidad) y CampañaDTO.
 * 
 * <p>Gestiona la transformación de datos relacionados con campañas,
 * incluyendo el manejo correcto de fechas LocalDate.</p>
 * 
 * @author Carlos
 * @version 2.0
 * @since 2025
 */
public class CampaniaMapper {

    /**
     * Constructor privado para evitar instanciación.
     */
    private CampaniaMapper() {
        throw new UnsupportedOperationException(
            "Esta es una clase utilitaria y no debe ser instanciada"
        );
    }

    /**
     * Convierte un DTO a una Entidad de Dominio.
     * 
     * <p>Maneja correctamente las fechas LocalDate para evitar
     * problemas de zona horaria y formato.</p>
     * 
     * @param dto objeto de transferencia de datos
     * @return entidad de dominio, o null si el DTO es null
     */
    public static Campania toEntity(CampaniaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new Campania(
            dto.getIdCampania(),
            dto.getNombreCampania(),
            dto.getTipoCampania(),
            dto.getFechaInicio(),
            dto.getFechaFin(),
            dto.getSupervisoresCargo(),
            dto.getDescripcionObjetivos()
        );
    }

    /**
     * Convierte una Entidad de Dominio a un DTO.
     * 
     * @param entity entidad de dominio
     * @return DTO correspondiente, o null si la entidad es null
     */
    public static CampaniaDTO toDTO(Campania entity) {
        if (entity == null) {
            return null;
        }
        
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