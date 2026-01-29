package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteService implements IClienteService {

    private final IClienteRepository clienteRepository;

    public ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void guardar(ClienteDTO dto) {
        Cliente cliente = new Cliente(
                dto.getIdCliente(),
                dto.getNombreCompleto(),
                dto.getDocumento(),
                dto.getTelefono()
        );
        clienteRepository.guardar(cliente);
    }

    @Override
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.listarTodos()
                .stream()
                .map(c -> {
                    ClienteDTO dto = new ClienteDTO();
                    dto.setIdCliente(c.getIdCliente());
                    dto.setNombreCompleto(c.getNombreCompleto());
                    dto.setDocumento(c.getDocumento());
                    dto.setTelefono(c.getTelefono());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        clienteRepository.eliminar(id);
    }
}
