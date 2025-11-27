/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;

/**
 * Mapper para conversiones entre Llamada (Entidad) y LlamadaDTO.
 * 
 * <p>Este mapper es especial porque maneja tanto los IDs de las
 * relaciones como los nombres descriptivos (que vienen del JOIN
 * en la consulta SQL).</p>
 * 
 * <p><b>Importante:</b> El método toDTO no mapea los nombres
 * porque esos vienen directamente desde el repositorio mediante
 * un JOIN optimizado.</p>
 * 
 * @author Carlos
 * @version 2.0
 * @since 2025
 */
public class LlamadaMapper {

    /**
     * Constructor privado para evitar instanciación.
     */
    private LlamadaMapper() {
        throw new UnsupportedOperationException(
            "Esta es una clase utilitaria y no debe ser instanciada"
        );
    }

    /**
     * Convierte un DTO a una Entidad de Dominio.
     * 
     * <p>Solo mapea los campos propios de la entidad Llamada,
     * ignorando los campos adicionales del DTO como nombreAgente,
     * nombreCliente, etc., ya que esos son solo para visualización.</p>
     * 
     * @param dto objeto de transferencia de datos
     * @return entidad de dominio, o null si el DTO es null
     */
    public static Llamada toEntity(LlamadaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new Llamada(
            dto.getIdLlamada(),
            dto.getFechaHora(),
            dto.getDuracion(),
            dto.getDetalleResultado(),
            dto.getObservaciones(),
            dto.getIdAgente(),
            dto.getIdCampania(),
            dto.getIdCliente()
        );
    }

    /**
     * Convierte una Entidad de Dominio a un DTO.
     * 
     * <p><b>NOTA IMPORTANTE:</b> Este método NO mapea los nombres
     * descriptivos (nombreAgente, nombreCliente, nombreCampania)
     * porque esos se obtienen directamente desde el repositorio
     * mediante un JOIN en SQL.</p>
     * 
     * <p>Si necesitas un DTO con nombres, usa el método
     * {@code repositorio.listarLlamadasConNombres()} que ya trae
     * esos datos.</p>
     * 
     * @param entity entidad de dominio
     * @return DTO básico (sin nombres), o null si la entidad es null
     */
    public static LlamadaDTO toDTO(Llamada entity) {
        if (entity == null) {
            return null;
        }
        
        LlamadaDTO dto = new LlamadaDTO();
        
        // Mapeo de campos básicos
        dto.setIdLlamada(entity.getIdLlamada());
        dto.setFechaHora(entity.getFechaHora());
        dto.setDuracion(entity.getDuracion());
        dto.setDetalleResultado(entity.getDetalleResultado());
        dto.setObservaciones(entity.getObservaciones());
        
        // Mapeo de IDs de relaciones
        dto.setIdAgente(entity.getIdAgente());
        dto.setIdCampania(entity.getIdCampania());
        dto.setIdCliente(entity.getIdCliente());
        
        // Los nombres (nombreAgente, nombreCliente, nombreCampania)
        // NO se mapean aquí porque vienen del repositorio mediante JOIN
        
        return dto;
    }
}