package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de Cliente - Versión KISS
 * SIMPLIFICADO: Conversión correcta DTO <-> Entidad
 */
public class ClienteService implements IClienteService {

    private final IClienteRepository clienteRepository;

    public ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void guardar(ClienteDTO dto) {
        // CORRECCIÓN: Usar constructor completo
        Cliente cliente = new Cliente(
                dto.getIdCliente(),
                dto.getNombreCompleto(),
                dto.getDocumentoIdentidad(),
                dto.getTelefono(),
                dto.getEmail(),
                dto.getDireccion()
        );
        clienteRepository.guardar(cliente);
    }

    @Override
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.listarTodos()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.eliminar(id);
    }

    // Método auxiliar para convertir Entidad -> DTO
    private ClienteDTO convertirADTO(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(c.getIdCliente());
        dto.setNombreCompleto(c.getNombreCompleto());
        dto.setDocumentoIdentidad(c.getDocumentoIdentidad());
        dto.setTelefono(c.getTelefono());
        dto.setEmail(c.getEmail());
        dto.setDireccion(c.getDireccion());
        return dto;
    }
}