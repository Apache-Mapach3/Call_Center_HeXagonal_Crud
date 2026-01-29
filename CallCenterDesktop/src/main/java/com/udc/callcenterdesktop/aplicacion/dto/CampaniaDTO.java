package com.udc.callcenterdesktop.aplicacion.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DTO para transportar datos de Campaña.
 */
public class CampaniaDTO {
    
    private Long idCampania;
    private String nombreCampania;
    private String tipoCampania;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String supervisoresCargo;
    private String descripcionObjetivos;

    public CampaniaDTO() {}

    public CampaniaDTO(Long id, String nombre, String tipo, LocalDate inicio, 
                       LocalDate fin, String supervisor, String descripcion) {
        this.idCampania = id;
        this.nombreCampania = nombre;
        this.tipoCampania = tipo;
        this.fechaInicio = inicio;
        this.fechaFin = fin;
        this.supervisoresCargo = supervisor;
        this.descripcionObjetivos = descripcion;
    }

    // Getters y Setters Compactos
    public Long getIdCampania() { return idCampania; }
    public void setIdCampania(Long idCampania) { this.idCampania = idCampania; }

    public String getNombreCampania() { return nombreCampania; }
    public void setNombreCampania(String nombreCampania) { this.nombreCampania = nombreCampania; }

    public String getTipoCampania() { return tipoCampania; }
    public void setTipoCampania(String tipoCampania) { this.tipoCampania = tipoCampania; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getSupervisoresCargo() { return supervisoresCargo; }
    public void setSupervisoresCargo(String supervisoresCargo) { this.supervisoresCargo = supervisoresCargo; }

    public String getDescripcionObjetivos() { return descripcionObjetivos; }
    public void setDescripcionObjetivos(String descripcionObjetivos) { this.descripcionObjetivos = descripcionObjetivos; }

    // Utilidad para la Vista
    public String getRangoFechas() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return (fechaInicio != null ? fechaInicio.format(fmt) : "N/A") + " - " + 
               (fechaFin != null ? fechaFin.format(fmt) : "Indefinida");
    }

    @Override
    public String toString() { return nombreCampania; }

    public void setDescripcion(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getDescripcion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}