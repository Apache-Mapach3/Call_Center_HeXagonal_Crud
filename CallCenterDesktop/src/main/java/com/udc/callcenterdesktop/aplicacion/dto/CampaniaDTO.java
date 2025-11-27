/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Data Transfer Object para la entidad Campaña.
 * 
 * <p>Facilita el transporte de información de campañas entre
 * las diferentes capas de la aplicación sin exponer la entidad
 * de dominio directamente.</p>
 * 
 * @author Carlos 
 * @version 1.0
 * @since 2025
 */
public class CampaniaDTO {
    
    private Long idCampania;
    private String nombreCampania;
    private String tipoCampania;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String supervisoresCargo;
    private String descripcionObjetivos;

    /**
     * Constructor vacío requerido por frameworks.
     */
    public CampaniaDTO() {
    }

    /**
     * Constructor completo para facilitar la creación de instancias.
     * 
     * @param id identificador único
     * @param nombre nombre de la campaña
     * @param tipo tipo de campaña
     * @param inicio fecha de inicio
     * @param fin fecha de finalización
     * @param supervisor responsables de la campaña
     * @param descripcion objetivos y descripción
     */
    public CampaniaDTO(Long id, String nombre, String tipo, 
                       LocalDate inicio, LocalDate fin, 
                       String supervisor, String descripcion) {
        this.idCampania = id;
        this.nombreCampania = nombre;
        this.tipoCampania = tipo;
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.supervisoresCargo = supervisor;
        this.descripcionObjetivos = descripcion;
    }

    //  GETTERS Y SETTERS

    public Long getIdCampania() { 
        return idCampania; 
    }

    public void setIdCampania(Long idCampania) { 
        this.idCampania = idCampania; 
    }

    public String getNombreCampania() { 
        return nombreCampania; 
    }

    public void setNombreCampania(String nombreCampania) { 
        this.nombreCampania = nombreCampania; 
    }

    public String getTipoCampania() { 
        return tipoCampania; 
    }

    public void setTipoCampania(String tipoCampania) { 
        this.tipoCampania = tipoCampania; 
    }

    public LocalDate getFechaInicio() { 
        return fechaInicio; 
    }

    public void setFechaInicio(LocalDate fechaInicio) { 
        this.fechaInicio = fechaInicio; 
    }

    public LocalDate getFechaFin() { 
        return fechaFin; 
    }

    public void setFechaFin(LocalDate fechaFin) { 
        this.fechaFin = fechaFin; 
    }

    public String getSupervisoresCargo() { 
        return supervisoresCargo; 
    }

    public void setSupervisoresCargo(String supervisoresCargo) { 
        this.supervisoresCargo = supervisoresCargo; 
    }

    public String getDescripcionObjetivos() { 
        return descripcionObjetivos; 
    }

    public void setDescripcionObjetivos(String descripcionObjetivos) { 
        this.descripcionObjetivos = descripcionObjetivos; 
    }

    // MÉTODOS DE UTILIDAD

    /**
     * Formatea las fechas en un rango legible.
     * 
     * @return string como "01/01/2024 - 31/12/2024"
     */
    public String getRangoFechas() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String inicio = fechaInicio != null ? fechaInicio.format(formatter) : "N/A";
        String fin = fechaFin != null ? fechaFin.format(formatter) : "Indefinida";
        return inicio + " - " + fin;
    }

    @Override
    public String toString() {
        return "CampaniaDTO{" +
                "idCampania=" + idCampania +
                ", nombreCampania='" + nombreCampania + '\'' +
                ", tipoCampania='" + tipoCampania + '\'' +
                '}';
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

