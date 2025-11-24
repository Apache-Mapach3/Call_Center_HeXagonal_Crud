/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.AgenteMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lógica de Negocio.
 * Implementa las validaciones y coordina el flujo de datos.
 */


/**
 * Servicio de Aplicación para Agentes.
 * Orquesta la lógica de negocio y las validaciones.
 */
public class AgenteService implements IAgenteService {

    private final IAgenteRepository repositorio;

    // Inyección de dependencias por constructor
    public AgenteService(IAgenteRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarAgente(AgenteDTO dto) {
        Agente entidad = AgenteMapper.toEntity(dto);
        validar(entidad);
        // Regla de negocio: Verificar si el número de empleado ya existe podría ir aquí
        repositorio.guardar(entidad);
    }

    @Override
    public List<AgenteDTO> listarAgentes() {
        return repositorio.listarTodos().stream()
                .map(AgenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarAgente(AgenteDTO dto) {
        if (dto.id == null) {
            throw new CallCenterException("No se puede actualizar un agente sin ID.");
        }
        Agente entidad = AgenteMapper.toEntity(dto);
        validar(entidad);
        repositorio.actualizar(entidad);
    }

    @Override
    public void eliminarAgente(Long id) {
        if (id == null) {
            throw new CallCenterException("ID de agente inválido.");
        }
        repositorio.eliminar(id);
    }
    
    // Validaciones de Negocio Puras (Sin interfaz gráfica)
    private void validar(Agente a) {
        if (a.getNombreCompleto() == null || a.getNombreCompleto().trim().isEmpty()) {
            throw new CallCenterException("El nombre del agente es obligatorio.");
        }
        if (a.getNumeroEmpleado() == null || a.getNumeroEmpleado().trim().isEmpty()) {
            throw new CallCenterException("El número de empleado es obligatorio.");
        }
        if (a.getEmail() != null && !a.getEmail().isEmpty() && !a.getEmail().contains("@")) {
            throw new CallCenterException("El formato del correo electrónico no es válido.");
        }
    }
}