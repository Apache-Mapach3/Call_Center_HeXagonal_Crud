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

    public void guardar(AgenteDTO dto) {
        Agente agente = AgenteMapper.toEntity(dto);
        if (dto.getIdAgente() != null) { // Corregido de getId() a getIdAgente()
            repositorio.actualizar(agente);
        } else {
            repositorio.guardar(agente);
        }
    }

    public List<AgenteDTO> listarTodos() {
        return repositorio.listarTodos().stream()
                .map(AgenteMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    // Implementa eliminar y buscar si tu interfaz lo requiere

    @Override
    public void registrarAgente(AgenteDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<AgenteDTO> listarAgentes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void actualizarAgente(AgenteDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eliminarAgente(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}