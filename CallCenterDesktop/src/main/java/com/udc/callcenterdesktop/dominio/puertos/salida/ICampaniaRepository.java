/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.salida;

/**
 *
 * @author camolano
 */
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import java.util.List;

/**
 * Puerto de Salida (Output Port).
 * Define el contrato que debe cumplir cualquier adaptador de persistencia.
 */
public interface ICampaniaRepository {
    void guardar(Campania campania);
    List<Campania> listarTodos();
    void actualizar(Campania campania);
    void eliminar(Long id);
}