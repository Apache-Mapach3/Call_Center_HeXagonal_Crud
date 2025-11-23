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
 * Data Transfer Object (DTO) para Cliente.
 * Se utiliza para transportar datos entre la capa de Presentación (Vista)
 * y la capa de Aplicación, evitando exponer la entidad de dominio directamente.
 */
public class ClienteDTO {
    
    public Long idCliente;
    public String nombreCompleto;
    public String documentoIdentidad;
    public String telefono;
    public String email;
    public String direccion;

    public ClienteDTO() {
    }

    public ClienteDTO(Long id, String nombre, String doc, String tel, String email, String dir) {
        this.idCliente = id;
        this.nombreCompleto = nombre;
        this.documentoIdentidad = doc;
        this.telefono = tel;
        this.email = email;
        this.direccion = dir;
    }
}