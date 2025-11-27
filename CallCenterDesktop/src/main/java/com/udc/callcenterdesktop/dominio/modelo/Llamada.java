/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidad de dominio que representa una Llamada registrada en el sistema.
 * 
 * <p>Una llamada es la interacción entre un agente y un cliente
 * dentro del contexto de una campaña específica. Registra detalles
 * importantes como duración, resultado y observaciones.</p>
 * 
 * <p><b>Reglas de negocio:</b></p>
 * <ul>
 *   <li>Debe estar asociada a un agente, cliente y campaña válidos</li>
 *   <li>La duración debe ser mayor a 0 segundos</li>
 *   <li>El detalle del resultado es obligatorio</li>
 *   <li>La fecha/hora se registra automáticamente al momento de la llamada</li>
 * </ul>
 * 
 * @author Carlos 
 * @version 1.0
 * @since 2025
 */
public class Llamada {

    private Long idLlamada;
    private LocalDateTime fechaHora;
    private Integer duracion;
    private String detalleResultado;
    private String observaciones;

    // Referencias a otras entidades (IDs foráneas)
    private Long idAgente;
    private Long idCampania;
    private Long idCliente;

    /**
     * Constructor vacío requerido por frameworks.
     */
    public Llamada() {
    }

    /**
     * Constructor completo para crear una llamada.
     * 
     * @param idLlamada identificador único
     * @param fechaHora fecha y hora exacta de la llamada
     * @param duracion duración en segundos
     * @param detalleResultado resultado de la llamada (Venta, No contesta, etc.)
     * @param observaciones notas adicionales del agente
     * @param idAgente ID del agente que atendió
     * @param idCampania ID de la campaña asociada
     * @param idCliente ID del cliente contactado
     */
    public Llamada(Long idLlamada, LocalDateTime fechaHora, Integer duracion, 
                   String detalleResultado, String observaciones, 
                   Long idAgente, Long idCampania, Long idCliente) {
        this.idLlamada = idLlamada;
        this.fechaHora = fechaHora;
        this.duracion = duracion;
        this.detalleResultado = detalleResultado;
        this.observaciones = observaciones;
        this.idAgente = idAgente;
        this.idCampania = idCampania;
        this.idCliente = idCliente;
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

    //MÉTODOS DE UTILIDAD

    /**
     * Formatea la duración en un formato legible (MM:SS).
     * 
     * @return duración formateada como "05:30" (5 minutos 30 segundos)
     */
    public String getDuracionFormateada() {
        if (duracion == null) return "00:00";
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    /**
     * Obtiene la fecha en formato corto legible.
     * 
     * @return fecha formateada como "26/11/2024 14:30"
     */
    public String getFechaHoraFormateada() {
        if (fechaHora == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaHora.format(formatter);
    }

    @Override
    public String toString() {
        return "Llamada{" +
                "idLlamada=" + idLlamada +
                ", fechaHora=" + fechaHora +
                ", duracion=" + duracion + "s" +
                ", detalleResultado='" + detalleResultado + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Llamada llamada = (Llamada) o;
        return idLlamada != null && idLlamada.equals(llamada.idLlamada);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}