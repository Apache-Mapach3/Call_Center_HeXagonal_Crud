/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Data Transfer Object para la entidad Llamada.
 * 
 * <p>Este DTO incluye tanto los IDs de las entidades relacionadas
 * como sus nombres descriptivos para facilitar la visualización
 * en las interfaces gráficas sin necesidad de hacer JOINs adicionales.</p>
 * 
 * @author Carlos Martinez
 * @version 1.0
 * @since 2024
 */
public class LlamadaDTO {
    
    private Long idLlamada;
    private LocalDateTime fechaHora;
    private Integer duracion;
    private String detalleResultado;
    private String observaciones;

    // IDs de las entidades relacionadas
    private Long idAgente;
    private Long idCampania;
    private Long idCliente;

    // Nombres descriptivos (para mostrar en tablas)
    private String nombreAgente;
    private String nombreCampania;
    private String nombreCliente;

    /**
     * Constructor vacío requerido por frameworks.
     */
    public LlamadaDTO() {
    }

    // GETTERS Y SETTERS

    public Long getIdLlamada() { 
        return idLlamada; 
    }

    public void setIdLlamada(Long idLlamada) { 
        this.idLlamada = idLlamada; 
    }

    public LocalDateTime getFechaHora() { 
        return fechaHora; 
    }

    public void setFechaHora(LocalDateTime fechaHora) { 
        this.fechaHora = fechaHora; 
    }

    public Integer getDuracion() { 
        return duracion; 
    }

    public void setDuracion(Integer duracion) { 
        this.duracion = duracion; 
    }

    public String getDetalleResultado() { 
        return detalleResultado; 
    }

    public void setDetalleResultado(String detalleResultado) { 
        this.detalleResultado = detalleResultado; 
    }

    public String getObservaciones() { 
        return observaciones; 
    }

    public void setObservaciones(String observaciones) { 
        this.observaciones = observaciones; 
    }

    public Long getIdAgente() { 
        return idAgente; 
    }

    public void setIdAgente(Long idAgente) { 
        this.idAgente = idAgente; 
    }

    public Long getIdCampania() { 
        return idCampania; 
    }

    public void setIdCampania(Long idCampania) { 
        this.idCampania = idCampania; 
    }

    public Long getIdCliente() { 
        return idCliente; 
    }

    public void setIdCliente(Long idCliente) { 
        this.idCliente = idCliente; 
    }

    public String getNombreAgente() { 
        return nombreAgente; 
    }

    public void setNombreAgente(String nombreAgente) { 
        this.nombreAgente = nombreAgente; 
    }

    public String getNombreCampania() { 
        return nombreCampania; 
    }

    public void setNombreCampania(String nombreCampania) { 
        this.nombreCampania = nombreCampania; 
    }

    public String getNombreCliente() { 
        return nombreCliente; 
    }

    public void setNombreCliente(String nombreCliente) { 
        this.nombreCliente = nombreCliente; 
    }

    // MÉTODOS DE UTILIDAD 

    /**
     * Formatea la duración en formato legible (MM:SS).
     * 
     * @return duración como "05:30"
     */
    public String getDuracionFormateada() {
        if (duracion == null) return "00:00";
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    /**
     * Formatea la fecha/hora en formato corto.
     * 
     * @return fecha como "26/11/2024 14:30"
     */
    public String getFechaHoraFormateada() {
        if (fechaHora == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHora.format(formatter);
    }

    @Override
    public String toString() {
        return "LlamadaDTO{" +
                "idLlamada=" + idLlamada +
                ", fechaHora=" + fechaHora +
                ", nombreAgente='" + nombreAgente + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                '}';
    }
}