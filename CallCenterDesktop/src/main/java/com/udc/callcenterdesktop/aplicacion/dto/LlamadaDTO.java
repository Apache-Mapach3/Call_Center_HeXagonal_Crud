/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;

import java.time.LocalDateTime;

public class LlamadaDTO {
    
    // Hacemos todo PUBLIC para acceso directo (Patrón DTO simple)
    public Long idLlamada;
    public LocalDateTime fechaHora;
    public Integer duracion;
    public String detalleResultado; // <--- Fíjate que este nombre coincida
    public String observaciones;

    // IDs
    public Long idAgente;
    public Long idCampania;
    public Long idCliente;

    // Nombres Auxiliares
    public String nombreAgente;
    public String nombreCampania;
    public String nombreCliente;

    public LlamadaDTO() {
    }
}