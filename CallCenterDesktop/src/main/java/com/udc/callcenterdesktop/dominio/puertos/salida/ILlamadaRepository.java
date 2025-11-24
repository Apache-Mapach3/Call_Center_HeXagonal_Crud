/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import java.util.List;

public interface ILlamadaRepository {
    
    // Método para guardar
    void registrar(Llamada llamada);
    
    // Método especial que devuelve DTOs con nombres
    List<LlamadaDTO> listarLlamadasConNombres();
}