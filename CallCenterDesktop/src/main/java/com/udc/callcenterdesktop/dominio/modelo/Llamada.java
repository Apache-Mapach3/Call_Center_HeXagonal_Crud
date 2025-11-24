/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

/**
 *
 * @author Admin
 */
import java.time.LocalDateTime;

public class Llamada {

    private Long idLlamada;
    private LocalDateTime fechaHora;
    private Integer duracion;
    private String detalleResultado;
    private String observaciones;

    // Relaciones (IDs)
    private Long idAgente;
    private Long idCampania;
    private Long idCliente;

    // Constructor Vacío
    public Llamada() {
    }

    // Constructor Completo (ESTE ES EL QUE BUSCA EL MAPPER)
    public Llamada(Long idLlamada, LocalDateTime fechaHora, Integer duracion, String detalleResultado, String observaciones, Long idAgente, Long idCampania, Long idCliente) {
        this.idLlamada = idLlamada;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.detalleResultado = detalleResultado;
        this.observaciones = observaciones;
        this.idAgente = idAgente;
        this.idCampania = idCampania;
        this.idCliente = idCliente;
    }

    // GETTERS Y SETTERS (NECESARIOS PARA EL MAPPER)
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
}