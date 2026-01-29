package com.udc.callcenterdesktop.dominio.puertos.entrada;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import java.util.List;

public interface IClienteService {

    void guardar(ClienteDTO dto);

    List<ClienteDTO> listarTodos();

    void eliminar(Long idCliente);
}
