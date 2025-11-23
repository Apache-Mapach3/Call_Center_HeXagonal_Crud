/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Agente;


public class AgenteMapper {

    
    public Agente toEntity(AgenteDTO dto) {
        Agente entity = new Agente();
        
       
        entity.setId(dto.getId() != null ? dto.getId().intValue() : 0);
        
        entity.setCodigoAgente(dto.getCodigoAgente());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEmail(dto.getEmail());
        entity.setEstado(dto.getEstado());
        
        return entity;
    }

    
    public AgenteDTO toDTO(Agente entity) {
        AgenteDTO dto = new AgenteDTO();
        
        // Mapeamos el ID: int de la Entidad a Long del DTO
        dto.setId((long) entity.getId());
        
        dto.setCodigoAgente(entity.getCodigoAgente());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        dto.setEstado(entity.getEstado());
        
        return dto;
    }
}