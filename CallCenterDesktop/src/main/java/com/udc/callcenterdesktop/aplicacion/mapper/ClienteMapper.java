/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;

/**
 * Clase utilitaria para la conversión entre Entidades y DTOs.
 * Facilita la transformación de datos sin ensuciar la lógica de negocio.
 */
public class ClienteMapper {

    /**
     * Convierte un DTO (Vista) a una Entidad (Dominio).
     */
    public static Cliente toEntity(ClienteDTO dto) {
        if (dto == null) return null;
        return new Cliente(
            dto.idCliente,
            dto.nombreCompleto,
            dto.documentoIdentidad,
            dto.telefono,
            dto.email,
            dto.direccion
        );
    }

    /**
     * Convierte una Entidad (Dominio) a un DTO (Vista).
     */
    public static ClienteDTO toDTO(Cliente entity) {
        if (entity == null) return null;
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