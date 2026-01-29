package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import java.util.List;
import java.util.Optional;
public interface ILlamadaRepository {

    void guardar(Llamada llamada);

    List<LlamadaDTO> listarLlamadasConNombres();

    List<Llamada> listarPorCliente(Long idCliente);

    public List<Llamada> listarTodas();
}
