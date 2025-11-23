/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;

/**
 * Data Transfer Object (DTO) para Agente.
 * Sirve para transportar datos desde la Vista hacia el Servicio
 * sin exponer la Entidad de Dominio directamente.
 */
public class AgenteDTO {
    
    // Usamos modificadores public para acceso directo (patrón común en DTOs puros)
    public Long id;
    public String nombre;
    public String numeroEmpleado;
    public String telefono;
    public String email;
    public String turno;
    public String experiencia;

    public AgenteDTO() {
    }

    // Constructor auxiliar para facilitar la creación
    public AgenteDTO(Long id, String nombre, String numeroEmpleado, String telefono, String email, String turno, String experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.numeroEmpleado = numeroEmpleado;
        this.telefono = telefono;
        this.email = email;
        this.turno = turno;
        this.experiencia = experiencia;
    }
}