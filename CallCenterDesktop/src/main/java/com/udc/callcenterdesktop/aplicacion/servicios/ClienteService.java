/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.ClienteMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la lógica de negocio para Clientes.
 * Orquesta el flujo de datos, realiza validaciones y llama al repositorio.
 */
public class ClienteService implements IClienteService {

    private final IClienteRepository repositorio;

    // Inyección de dependencias por constructor
    public ClienteService(IClienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarCliente(ClienteDTO dto) {
        // Validaciones de negocio
        if (dto.nombreCompleto == null || dto.nombreCompleto.trim().isEmpty()) {
            throw new CallCenterException("El nombre del cliente es obligatorio.");
        }
        if (dto.documentoIdentidad == null || dto.documentoIdentidad.trim().isEmpty()) {
            throw new CallCenterException("El documento de identidad es obligatorio.");
        }

        // Conversión y persistencia
        Cliente entidad = ClienteMapper.toEntity(dto);
        repositorio.guardar(entidad);
    }

    @Override
    public List<ClienteDTO> listarClientes() {
        // Recupera entidades, las convierte a DTOs y retorna la lista
        return repositorio.listarTodos().stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarCliente(ClienteDTO dto) {
        if (dto.idCliente == null) {
            throw new CallCenterException("No se puede actualizar un cliente sin ID.");
        }
        Cliente entidad = ClienteMapper.toEntity(dto);
        repositorio.actualizar(entidad);
    }

    @Override
    public void eliminarCliente(Long id) {
        if (id == null) {
            throw new CallCenterException("ID inválido para eliminación.");
        }
        repositorio.eliminar(id);
    }
}