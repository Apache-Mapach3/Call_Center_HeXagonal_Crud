/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

/**
 *
 * @author Admin
 */
/**
 * Entidad que representa un Cliente dentro del dominio del negocio.
 * Esta clase es pura y no debe contener anotaciones de base de datos ni lógica de interfaz.
 */
public class Cliente {
    
    private Long idCliente;
    private String nombreCompleto;
    private String documentoIdentidad;
    private String telefono;
    private String email;
    private String direccion;

    // Constructor vacío requerido por frameworks y utilidades
    public Cliente() {
    }

    // Constructor completo para instanciación rápida
    public Cliente(Long idCliente, String nombreCompleto, String documentoIdentidad, String telefono, String email, String direccion) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    // Métodos de Acceso (Getters y Setters)

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}