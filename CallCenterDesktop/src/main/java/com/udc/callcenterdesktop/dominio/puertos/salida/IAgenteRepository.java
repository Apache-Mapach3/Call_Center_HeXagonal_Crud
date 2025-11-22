/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.salida;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import java.util.List;

public interface IAgenteRepository {
    
    // Métodos para la Base de Datos
    void guardar(Agente agente);
    List<Agente> listarTodos();
    void actualizar(Agente agente);
    void eliminar(Long id);
}