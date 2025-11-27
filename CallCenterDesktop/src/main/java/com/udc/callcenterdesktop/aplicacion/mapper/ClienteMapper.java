/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;

/**
 * Mapper para conversiones entre Cliente (Entidad) y ClienteDTO.
 * 
 * <p>Facilita la transformación de datos entre la capa de dominio
 * y la capa de presentación, manteniendo el desacoplamiento
 * arquitectónico.</p>
 * 
 * @author Carlos
 * @version 2.0
 * @since 2025
 */
public class ClienteMapper {

    /**
     * Constructor privado para evitar instanciación.
     */
    private ClienteMapper() {
        throw new UnsupportedOperationException(
            "Esta es una clase utilitaria y no debe ser instanciada"
        );
    }

    /**
     * Convierte un DTO a una Entidad de Dominio.
     * 
     * @param dto objeto de transferencia de datos
     * @return entidad de dominio, o null si el DTO es null
     */
    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new Cliente(
            dto.getIdCliente(),
            dto.getNombreCompleto(),
            dto.getDocumentoIdentidad(),
            dto.getTelefono(),
            dto.getEmail(),
            dto.getDireccion()
        );
    }

    /**
     * Convierte una Entidad de Dominio a un DTO.
     * 
     * @param entity entidad de dominio
     * @return DTO correspondiente, o null si la entidad es null
     */
    public static ClienteDTO toDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }
        
        return new ClienteDTO(
            entity.getIdCliente(),
            entity.getNombreCompleto(),
            entity.getDocumentoIdentidad(),
            entity.getTelefono(),
            entity.getEmail(),
            entity.getDireccion()
        );
    }
}