package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;

/**
 * Mapper para Cliente - Versión KISS
 * SIMPLIFICADO: Usa constructor correcto y setters
 */
public class ClienteMapper {

    private ClienteMapper() {
        throw new UnsupportedOperationException(
            "Esta es una clase utilitaria y no debe ser instanciada"
        );
    }

    /**
     * Convierte un DTO a una Entidad de Dominio
     */
    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // CORRECCIÓN: Usar constructor completo con los parámetros correctos
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
     * Convierte una Entidad de Dominio a un DTO
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