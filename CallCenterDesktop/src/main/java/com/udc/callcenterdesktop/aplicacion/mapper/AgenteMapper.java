/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Agente;

/**
 * Traductor entre DTOs y Entidades.
 */
public class AgenteMapper {

    public static Agente toEntity(AgenteDTO dto) {
        if (dto == null) return null;
        return new Agente(dto.id, dto.nombre, dto.numeroEmpleado, dto.telefono, dto.email, dto.turno, dto.experiencia);
    }

    public static AgenteDTO toDTO(Agente entity) {
        if (entity == null) return null;
        return new AgenteDTO(entity.getIdAgente(), entity.getNombreCompleto(), entity.getNumeroEmpleado(), entity.getTelefonoContacto(), entity.getEmail(), entity.getHorarioTurno(), entity.getNivelExperiencia());
    }
}