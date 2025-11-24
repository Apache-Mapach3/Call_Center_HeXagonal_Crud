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

    // USAMOS CAMPOS PÚBLICOS
    public static Llamada toEntity(LlamadaDTO dto) {
        if (dto == null) return null;
        return new Llamada(
            dto.idLlamada,
            dto.fechaHora,
            dto.duracion,
            dto.detalleResultado,
            dto.observaciones,
            dto.idAgente,
            dto.idCampania,
            dto.idCliente
        );
    }

    public static LlamadaDTO toDTO(Llamada entity) {
        if (entity == null) return null;
        
        LlamadaDTO dto = new LlamadaDTO();
        // Asignación directa a campos públicos (sin set...)
        dto.idLlamada = entity.getIdLlamada();
        dto.fechaHora = entity.getFechaHora();
        dto.duracion = entity.getDuracion();
        dto.detalleResultado = entity.getDetalleResultado();
        dto.observaciones = entity.getObservaciones();
        
        dto.idAgente = entity.getIdAgente();
        dto.idCampania = entity.getIdCampania();
        dto.idCliente = entity.getIdCliente();
        
        return dto;
    }
}