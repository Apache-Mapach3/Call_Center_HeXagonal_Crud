package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.AgenteMapper;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import java.util.List;
import java.util.stream.Collectors;

public class AgenteService implements IAgenteService {

    private final IAgenteRepository repositorio;

    public AgenteService(IAgenteRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarAgente(AgenteDTO dto) {
        Agente agente = AgenteMapper.toEntity(dto);
        repositorio.guardar(agente);
    }

    @Override
    public List<AgenteDTO> listarAgentes() {
        return repositorio.listarTodos().stream()
                .map(AgenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarAgente(AgenteDTO dto) {
        Agente agente = AgenteMapper.toEntity(dto);
        repositorio.actualizar(agente);
    }

    @Override
    public void eliminarAgente(Long id) {
        repositorio.eliminar(id);
    }

    // Método adicional para compatibilidad
    public void guardar(AgenteDTO dto) {
        if (dto.getIdAgente() != null) {
            actualizarAgente(dto);
        } else {
            registrarAgente(dto);
        }
    }

    public List<AgenteDTO> listarTodos() {
        return listarAgentes();
    }
}