/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.CampaniaMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lógica de Negocio.
 * Implementa las validaciones y coordina el flujo de datos.
 */
public class CampaniaService implements ICampaniaService {

    private final ICampaniaRepository repositorio;

    public CampaniaService(ICampaniaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarCampania(CampaniaDTO dto) {
        Campania entidad = CampaniaMapper.toEntity(dto);
        validar(entidad);
        repositorio.guardar(entidad);
    }

    @Override
    public List<CampaniaDTO> listarCampanias() {
        return repositorio.listarTodos().stream()
                .map(CampaniaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarCampania(CampaniaDTO dto) {
        if (dto.idCampania == null) {
            throw new CallCenterException("ID de campaña requerido para actualizar.");
        }
        Campania entidad = CampaniaMapper.toEntity(dto);
        validar(entidad);
        repositorio.actualizar(entidad);
    }

    @Override
    public void eliminarCampania(Long id) {
        if (id == null) {
            throw new CallCenterException("ID nulo, no se puede eliminar.");
        }
        repositorio.eliminar(id);
    }
    
    private void validar(Campania c) {
        if (c.getNombreCampania() == null || c.getNombreCampania().trim().isEmpty()) {
            throw new CallCenterException("El nombre de la campaña es obligatorio.");
        }
        // Validación Lógica: La fecha fin no puede ser antes de la fecha inicio
        if (c.getFechaInicio() != null && c.getFechaFin() != null) {
            if (c.getFechaFin().isBefore(c.getFechaInicio())) {
                throw new CallCenterException("La fecha de finalización no puede ser anterior a la de inicio.");
            }
        }
    }
}