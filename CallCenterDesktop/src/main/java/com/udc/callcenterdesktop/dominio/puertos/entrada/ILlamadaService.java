/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;
    
    import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import java.util.List;
import java.util.Optional;

public interface ILlamadaService {

   
    LlamadaDTO crearOActualizarLlamada(LlamadaDTO llamadaDTO);

    
    Optional<LlamadaDTO> obtenerLlamadaPorId(int id);

   
    List<LlamadaDTO> obtenerTodasLlamadas();

    
    boolean eliminarLlamada(int id);
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

