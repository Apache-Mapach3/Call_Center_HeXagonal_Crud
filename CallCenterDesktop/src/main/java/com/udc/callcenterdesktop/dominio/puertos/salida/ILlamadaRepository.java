/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import java.util.List;
import java.util.Optional;

public interface ILlamadaRepository {

    
    Llamada guardar(Llamada llamada);

    
    Optional<Llamada> buscarPorId(int id);

    
    List<Llamada> buscarTodas();

    
    boolean eliminarPorId(int id);
}