/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.LlamadaMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;
import java.util.List;

/**
 * Lógica de Negocio para el registro de llamadas.
 * Valida las reglas y coordina la persistencia.
 */
public class LlamadaService implements ILlamadaService {

    private final ILlamadaRepository repositorio;

    public LlamadaService(ILlamadaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarLlamada(LlamadaDTO dto) {
        // Validaciones de Integridad
        if (dto.idAgente == null || dto.idCliente == null || dto.idCampania == null) {
            throw new CallCenterException("Error: Debe seleccionar un Agente, un Cliente y una Campaña válidos.");
        }
        
        // Validaciones de Negocio
        if (dto.duracion == null || dto.duracion <= 0) {
            throw new CallCenterException("La duración de la llamada debe ser mayor a 0 segundos.");
        }
        if (dto.detalleResultado == null || dto.detalleResultado.trim().isEmpty()) {
            throw new CallCenterException("El detalle del resultado es obligatorio.");
        }

        // Conversión y Guardado
        Llamada entidad = LlamadaMapper.toEntity(dto);
        repositorio.registrar(entidad);
    }

    @Override
    public List<LlamadaDTO> listarHistorial() {
        // Usa el método optimizado del repositorio que ya trae los nombres (JOINs)
        return repositorio.listarLlamadasConNombres();
    }
}