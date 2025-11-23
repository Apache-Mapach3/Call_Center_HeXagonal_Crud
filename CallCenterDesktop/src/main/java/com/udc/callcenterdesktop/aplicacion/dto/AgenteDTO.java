/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;

/**
 *
 * @author Admin
 */
/**
 * Data Transfer Object para Agente.
 * Objeto plano para transportar datos entre la Vista y el Servicio.
 */
public class AgenteDTO {
    public Long id;
    public String nombre;
    public String numeroEmpleado;
    public String telefono;
    public String email;
    public String turno;
    public String experiencia;

    public AgenteDTO() {}

    public AgenteDTO(Long id, String nombre, String num, String tel, String email, String turno, String exp) {
        this.id = id;
        this.nombre = nombre;
        this.numeroEmpleado = num;
        this.telefono = tel;
        this.email = email;
        this.turno = turno;
        this.experiencia = exp;
    }
}
