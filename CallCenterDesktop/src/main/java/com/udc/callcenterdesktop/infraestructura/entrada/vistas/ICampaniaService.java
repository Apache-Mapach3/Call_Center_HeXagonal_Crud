/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.dominio.modelo.Campania;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

/**
 *
 * @author camolano
 */
public  interface ICampaniaService {
    
    Campania crearOActualizarCampania(Campania campania);
    Optional<Campania> obtenerCampaniaPorId(int id);
    List<Campania> obtenerTodasCampanias();
    boolean eliminarCampania(int id);
    List<Campania> obtenerCampaniasActivas(LocalDate fechaActual);

    
}
