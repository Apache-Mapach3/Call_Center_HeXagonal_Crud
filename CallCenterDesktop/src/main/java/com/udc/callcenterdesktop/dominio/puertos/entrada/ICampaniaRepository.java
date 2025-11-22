/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author camolano
 */
public interface ICampaniaRepository {
    Campania crearOactualizarCampania(Campania campania);
    Optional<Campania>obtenerCamniaPorId(int id);
    List<Campania>obtenerTodasCampanias();
    boolean eliminarCampania(int id);
    List<Campania>obtenerCampaniasActivas(LocalDate fechaActual);
    
    
}
