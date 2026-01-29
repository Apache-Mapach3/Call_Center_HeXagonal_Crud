package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Agente;

public class AgenteMapper {

    public static Agente toEntity(AgenteDTO dto) {
        if (dto == null) return null;
        Agente agente = new Agente();
        agente.setIdAgente(dto.getIdAgente());
        agente.setNombreCompleto(dto.getNombreCompleto());
        agente.setCodigoEmpleado(dto.getCodigoEmpleado());
        agente.setEmail(dto.getEmail());
        agente.setTelefono(dto.getTelefono()); // Asumiendo que Agente tiene este campo
        return agente;
    }

    public static AgenteDTO toDTO(Agente entity) {
        if (entity == null) return null;
        AgenteDTO dto = new AgenteDTO();
        dto.setIdAgente(entity.getIdAgente());
        dto.setNombreCompleto(entity.getNombreCompleto());
        dto.setCodigoEmpleado(entity.getCodigoEmpleado());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        return dto;
    }
}