/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java 
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import java.util.List;


public interface ICampaniaRepository {

   
    void guardar(Campania campania);

    
    List<Campania> listarTodos();

    
    void actualizar(Campania campania);

    
    void eliminar(Long id);
    
}

