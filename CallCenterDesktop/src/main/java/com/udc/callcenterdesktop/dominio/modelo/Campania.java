package com.udc.callcenterdesktop.dominio.modelo;

import java.time.LocalDate;

/**
 * Entidad Campaña - Versión KISS
 * SIMPLIFICADO: Todos los métodos funcionan correctamente
 */
public class Campania {

    private Long idCampania;
    private String nombreCampania;
    private String tipoCampania;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String supervisoresCargo;
    private String descripcionObjetivos;

    // Constructor vacío
    public Campania() {
    }

    // Constructor completo
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

    // ===== GETTERS Y SETTERS =====

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

    // ===== ALIAS PARA COMPATIBILIDAD =====
    
    public String getDescripcionCampania() {
        return descripcionObjetivos;
    }

    public void setDescripcionCampania(String descripcion) {
        this.descripcionObjetivos = descripcion;
    }

    // ===== MÉTODOS DE UTILIDAD =====

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
                '}';
    }
}