/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO; // Importamos el DTO
import java.util.List;

public interface IAgenteService {
    // AHORA RECIBE Y DEVUELVE DTOs
    void registrarAgente(AgenteDTO agenteDto);
    List<AgenteDTO> listarAgentes();
    void actualizarAgente(AgenteDTO agenteDto);
    void eliminarAgente(Long id);
}
