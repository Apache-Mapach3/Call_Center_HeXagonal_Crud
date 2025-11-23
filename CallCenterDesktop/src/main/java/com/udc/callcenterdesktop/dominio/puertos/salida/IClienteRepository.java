/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import java.util.List;
import java.util.Optional;

public interface IClienteRepository {

   
    Cliente guardar(Cliente cliente);

  
    Optional<Cliente> buscarPorId(int id);


    List<Cliente> buscarTodos();

    
    boolean eliminarPorId(int id);
}