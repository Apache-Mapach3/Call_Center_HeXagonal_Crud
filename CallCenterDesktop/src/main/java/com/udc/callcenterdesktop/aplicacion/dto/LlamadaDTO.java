package com.udc.callcenterdesktop.aplicacion.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO que aplana la relación entre Llamada, Agente, Cliente y Campaña.
 */
public class LlamadaDTO {
    
    private Long idLlamada;
    private LocalDateTime fechaHora;
    private Integer duracion; // en segundos
    private String detalleResultado;
    private String observaciones;

    private Long idAgente;
    private Long idCampania;
    private Long idCliente;

    private String nombreAgente;
    private String nombreCampania;
    private String nombreCliente;

    public LlamadaDTO() {}

    // Getters y Setters Compactos
    public Long getIdLlamada() { return idLlamada; }
    public void setIdLlamada(Long idLlamada) { this.idLlamada = idLlamada; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }

    public String getDetalleResultado() { return detalleResultado; }
    public void setDetalleResultado(String detalleResultado) { this.detalleResultado = detalleResultado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public Long getIdAgente() { return idAgente; }
    public void setIdAgente(Long idAgente) { this.idAgente = idAgente; }

    public Long getIdCampania() { return idCampania; }
    public void setIdCampania(Long idCampania) { this.idCampania = idCampania; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getNombreAgente() { return nombreAgente; }
    public void setNombreAgente(String nombreAgente) { this.nombreAgente = nombreAgente; }

    public String getNombreCampania() { return nombreCampania; }
    public void setNombreCampania(String nombreCampania) { this.nombreCampania = nombreCampania; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    // Utilidades de Presentación
    public String getDuracionFormateada() {
        if (duracion == null) return "00:00";
        return String.format("%02d:%02d", duracion / 60, duracion % 60);
    }

    public String getFechaHoraFormateada() {
        if (fechaHora == null) return "";
        return fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return "Llamada #" + idLlamada + " - " + nombreCliente;
    }
}