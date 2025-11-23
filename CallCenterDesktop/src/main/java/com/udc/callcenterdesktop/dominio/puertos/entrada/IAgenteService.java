/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import java.util.List;

/**
 * Puerto de Entrada (Input Port).
 * Define las operaciones disponibles para la gestión de Agentes.
 * Usa DTOs para cumplir con la arquitectura Hexagonal estricta.
 */
public interface IAgenteService {
    
    // Recibe y devuelve DTOs (Data Transfer Objects), NO Entidades
    void registrarAgente(AgenteDTO dto);
    
    List<AgenteDTO> listarAgentes();
    
    void actualizarAgente(AgenteDTO dto);
    
    void eliminarAgente(Long id);
}