package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.LlamadaMapper;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class LlamadaService implements ILlamadaService {

    private final ILlamadaRepository llamadaRepository;

    public LlamadaService(ILlamadaRepository llamadaRepository) {
        this.llamadaRepository = llamadaRepository;
    }

    @Override
    public void registrar(LlamadaDTO dto) {
        Llamada llamada = LlamadaMapper.toEntity(dto);
        llamadaRepository.guardar(llamada);
    }

    @Override
    public List<LlamadaDTO> listarLlamadas() {
        // Opción 1: Si tu repositorio tiene un método con nombres
        try {
            return llamadaRepository.listarLlamadasConNombres();
        } catch (Exception e) {
            // Opción 2: Fallback - convertir las llamadas básicas
            return llamadaRepository.listarTodas()
                    .stream()
                    .map(LlamadaMapper::toDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<Llamada> listarTodas() {
        return llamadaRepository.listarTodas();
    }
}