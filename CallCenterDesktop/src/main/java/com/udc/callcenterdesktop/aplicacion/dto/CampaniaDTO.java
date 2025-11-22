/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;

import java.time.LocalDate;

public class CampaniaDTO {
    
    // Datos públicos para facilitar el acceso desde la vista
    public Long idCampania;
    public String nombreCampania;
    public String tipoCampania;
    public LocalDate fechaInicio;
    public LocalDate fechaFin;
    public String supervisoresCargo;
    public String descripcionObjetivos;

    // Constructor vacío
    public CampaniaDTO() {
    }

    // Constructor completo
    public CampaniaDTO(Long idCampania, String nombreCampania, String tipoCampania, LocalDate fechaInicio, LocalDate fechaFin, String supervisoresCargo, String descripcionObjetivos) {
        this.idCampania = idCampania;
        this.nombreCampania = nombreCampania;
        this.tipoCampania = tipoCampania;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.supervisoresCargo = supervisoresCargo;
        this.descripcionObjetivos = descripcionObjetivos;
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

