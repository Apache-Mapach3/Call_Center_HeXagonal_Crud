/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

import java.time.LocalDate;

/**
 * Entidad pura del negocio: Campaña.
 * Representa una estrategia de marketing o servicio.
 * No debe tener anotaciones de BD ni librerías visuales.
 */
public class Campania {

    private Long idCampania;
    private String nombreCampania;
    private String tipoCampania;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String supervisoresCargo;
    private String descripcionObjetivos;

    public Campania() {
    }

    public Campania(Long idCampania, String nombreCampania, String tipoCampania, LocalDate fechaInicio, LocalDate fechaFin, String supervisoresCargo, String descripcionObjetivos) {
        this.idCampania = idCampania;
        this.nombreCampania = nombreCampania;
        this.tipoCampania = tipoCampania;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.supervisoresCargo = supervisoresCargo;
        this.descripcionObjetivos = descripcionObjetivos;
    }

    // Getters y Setters
    public Long getIdCampania() { return idCampania; }
    public void setIdCampania(Long idCampania) { this.idCampania = idCampania; }

    public String getNombreCampania() { return nombreCampania; }
    public void setNombreCampania(String nombreCampania) { this.nombreCampania = nombreCampania; }

    public String getTipoCampania() { return tipoCamp