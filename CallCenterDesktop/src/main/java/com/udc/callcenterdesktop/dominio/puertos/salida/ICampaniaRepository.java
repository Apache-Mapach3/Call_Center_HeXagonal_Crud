/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Campania;
import java.util.List;

/**
 * Puerto de Salida (Output Port) para la persistencia de Campañas.
 * Define el contrato que debe cumplir cualquier adaptador de persistencia.
 * 
 * <p>Este puerto sigue el principio de Inversión de Dependencias,
 * permitiendo que el dominio no dependa de la infraestructura.</p>
 * 
 * @author Carlos 
 * @version 1.0
 * @since 2025
 */
public interface ICampaniaRepository {
    
    /**
     * Persiste una nueva campaña en el almacenamiento de datos.
     * 
     * @param campania entidad de dominio a guardar
     */
    void guardar(Campania campania);
    
    /**
     * Recupera todas las campañas registradas.
     * 
     * @return lista de campañas
     */
    List<Campania> listarTodos();
    
    /**
     * Actualiza los datos de una campaña existente.
     * 
     * @param campania entidad con datos modificados
     */
    void actualizar(Campania campania);
    
    /**
     * Elimina una campaña del sistema.
     * 
     * @param id identificador único de la campaña
     */
    void eliminar(Long id);
}