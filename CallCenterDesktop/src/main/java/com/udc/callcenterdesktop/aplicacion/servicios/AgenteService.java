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
public class AgenteService implements IAgenteService {

    private final IAgenteRepository repositorio;

    public AgenteService(IAgenteRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarAgente(AgenteDTO dto) {
        Agente entidad = AgenteMapper.toEntity(dto);
        validar(entidad);
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
        if (dto.id == null) throw new CallCenterException("ID necesario para actualizar.");
        Agente entidad = AgenteMapper.toEntity(dto);
        validar(entidad);
        repositorio.actualizar(entidad);
    }

    @Override
    public void eliminarAgente(Long id) {
        if (id == null) throw new CallCenterException("ID nulo.");
        // NOTA: No ponemos JOptionPane aquí. La confirmación visual se hace en la Vista.
        repositorio.eliminar(id);
    }
    
    // Validaciones de negocio usando Excepciones (Correcto)
    private void validar(Agente a) {
        if (a.getNombreCompleto() == null || a.getNombreCompleto().isEmpty()) {
            throw new CallCenterException("El nombre es obligatorio.");
        }
        if (a.getEmail() == null || !a.getEmail().contains("@")) {
            throw new CallCenterException("El email no es válido.");
        }
    }
}