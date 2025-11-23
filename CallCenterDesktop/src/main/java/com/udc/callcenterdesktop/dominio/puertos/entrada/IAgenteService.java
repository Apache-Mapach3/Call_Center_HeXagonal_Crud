/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import java.util.List;

/**
 * Puerto de Entrada ACTUALIZADO.
 * Ahora usa DTOs para cumplir con la arquitectura Hexagonal estricta.
 */
public interface IAgenteService {
    
    // Todos los métodos deben recibir/devolver DTOs, NO Entidades
    void registrarAgente(AgenteDTO dto);
    
    List<AgenteDTO> listarAgentes();
    
    void actualizarAgente(AgenteDTO dto);
    
    void eliminarAgente(Long id);
}