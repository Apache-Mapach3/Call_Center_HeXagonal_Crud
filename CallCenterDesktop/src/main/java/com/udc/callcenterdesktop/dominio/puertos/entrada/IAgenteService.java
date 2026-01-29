package com.udc.callcenterdesktop.dominio.puertos.entrada;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import java.util.List;

public interface IAgenteService {

    void registrarAgente(AgenteDTO dto);

    List<AgenteDTO> listarAgentes();

    void actualizarAgente(AgenteDTO dto);

    void eliminarAgente(Long id);

    public void guardar(AgenteDTO dto);

    public Iterable<AgenteDTO> listarTodos();
}
