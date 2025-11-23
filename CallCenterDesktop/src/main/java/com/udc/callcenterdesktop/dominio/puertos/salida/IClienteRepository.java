/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import java.util.List;

/**
 * Puerto de Salida (Output Port).
 * Define el contrato para la persistencia de Clientes.
 * Desacopla el dominio de la tecnología de base de datos específica.
 */
public interface IClienteRepository {
    
    /**
     * Persiste un nuevo cliente en el almacenamiento de datos.
     * @param cliente Objeto de dominio a guardar.
     */
    void guardar(Cliente cliente);

    /**
     * Recupera todos los clientes registrados.
     * @return Lista de objetos Cliente.
     */
    List<Cliente> listarTodos();

    /**
     * Actualiza la información de un cliente existente.
     * @param cliente Objeto con datos modificados.
     */
    void actualizar(Cliente cliente);

    /**
     * Elimina un cliente del sistema basado en su ID.
     * @param id Identificador único del cliente.
     */
    void eliminar(Long id);
}