/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

import java.time.LocalDate;

/**
 * Entidad de dominio que representa una Campaña de Marketing o Servicio.
 * 
 * <p>Una campaña es una estrategia organizada con objetivos específicos
 * que se ejecuta durante un período determinado. Puede ser de ventas,
 * soporte técnico, encuestas de satisfacción, entre otros.</p>
 * 
 * <p><b>Reglas de negocio:</b></p>
 * <ul>
 *   <li>El nombre de la campaña debe ser único</li>
 *   <li>La fecha de fin no puede ser anterior a la fecha de inicio</li>
 *   <li>Se requiere al menos un supervisor asignado</li>
 *   <li>Los objetivos deben estar claramente definidos</li>
 * </ul>
 * 
 * @author Carlos 
 * @version 1.0
 * @since 2025
 */
public class Campania {

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
    public Campania() {
    }

    /**
     * Constructor completo para crear una campaña.
     * 
     * @param idCampania identificador único
     * @param nombreCampania nombre descriptivo de la campaña
     * @param tipoCampania tipo (Ventas, Soporte, Encuesta, Retención)
     * @param fechaInicio fecha de inicio de la campaña
     * @param fechaFin fecha de finalización (puede ser null si es indefinida)
     * @param supervisoresCargo nombres de los supervisores responsables
     * @param descripcionObjetivos descripción detallada de los objetivos
     */
    public Campania(Long idCampania, String nombreCampania, String tipoCampania, 
                    LocalDate fechaInicio, LocalDate fechaFin, 
                    String supervisoresCargo, String descripcionObjetivos) {
        this.idCampania = idCampania;
        this.nombreCampania = nombreCampania;
        this.tipoCampania = tipoCampania;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.supervisoresCargo = supervisoresCargo;
        this.descripcionObjetivos = descripcionObjetivos;
    }

    // GETTERS Y SETTERS 

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

    //MÉTODOS DE UTILIDAD

    /**
     * Verifica si la campaña está actualmente activa.
     * 
     * @return true si la fecha actual está entre inicio y fin
     */
    public boolean estaActiva() {
        LocalDate hoy = LocalDate.now();
        boolean despuesDeInicio = fechaInicio == null || !hoy.isBefore(fechaInicio);
        boolean antesDelFin = fechaFin == null || !hoy.isAfter(fechaFin);
        return despuesDeInicio && antesDelFin;
    }

    @Override
    public String toString() {
        return "Campania{" +
                "idCampania=" + idCampania +
                ", nombreCampania='" + nombreCampania + '\'' +
                ", tipoCampania='" + tipoCampania + '\'' +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campania campania = (Campania) o;
        return idCampania != null && idCampania.equals(campania.idCampania);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}