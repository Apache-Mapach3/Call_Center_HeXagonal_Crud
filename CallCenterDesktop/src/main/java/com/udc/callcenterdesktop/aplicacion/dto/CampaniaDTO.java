/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;
/**
 *
 * @author camolano
 */
import java.time.LocalDate;

/**
 * Data Transfer Object para Campaña.
 * Aísla la vista del modelo de dominio.
 */
public class CampaniaDTO {
    
    public Long idCampania;
    public String nombreCampania;
    public String tipoCampania;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;
    public String supervisoresCargo;
    public String descripcionObjetivos;

    public CampaniaDTO() {
    }

    public CampaniaDTO(Long id, String nombre, String tipo, LocalDate inicio, LocalDate fin, String supervisor, String descripcion) {
        this.idCampania = id;
        this.nombreCampania = nombre;
        this.tipoCampania = tipo;
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.supervisoresCargo = supervisor;
        this.descripcionObjetivos = descripcion;
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

