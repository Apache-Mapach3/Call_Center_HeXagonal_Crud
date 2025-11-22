/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;

import com.udc.callcenterdesktop.dominio.modelo.Campania;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de Entrada (Input Port / Service Interface).
 * Define las operaciones de negocio que la capa de Presentación (Vistas) puede invocar.
 * La Vista SOLO debe conocer esta interfaz, no la implementación.
 */
public interface ICampaniaService {
    
    /**
     * Crea o actualiza una campaña, aplicando reglas de negocio.
     * @param campania La Campaña a persistir.
     * @return La Campaña guardada/actualizada.
     */
    Campania crearOActualizarCampania(Campania campania);
    
    /**
     * Obtiene una campaña por su identificador.
     * @param id El ID de la campaña.
     * @return Un Optional<Campania> para manejar la posible ausencia.
     */
    Optional<Campania> obtenerCampaniaPorId(int id);
    
    /**
     * Obtiene todas las campañas registradas.
     * @return Una lista de campañas.
     */
    List<Campania> obtenerTodasCampanias();
    
    /**
     * Elimina una campaña aplicando reglas de negocio.
     * @param id El ID de la campaña a eliminar.
     * @return true si la eliminación fue exitosa.
     */
    boolean eliminarCampania(int id);
    
    /**
     * Obtiene campañas activas según una fecha.
     */
    List<Campania> obtenerCampaniasActivas(LocalDate fechaActual);
}
    

