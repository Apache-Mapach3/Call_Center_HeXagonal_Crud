/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import java.util.List;

public interface IAgenteService {
    
    // Métodos para la Vista
    void registrarAgente(Agente agente);
    List<Agente> listarAgentes();
    void actualizarAgente(Agente agente);
    void eliminarAgente(Long id);
}
