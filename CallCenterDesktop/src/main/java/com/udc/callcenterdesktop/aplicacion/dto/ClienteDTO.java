/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;

/**
 * Data Transfer Object para la entidad Cliente.
 * 
 * <p>Se utiliza para transportar datos de clientes entre la capa de
 * presentación (vistas Swing) y la capa de aplicación (servicios),
 * manteniendo el desacoplamiento arquitectónico.</p>
 * 
 * @author Carlos 
 * @version 1.0
 * @since 2025
 */
public class ClienteDTO {
    
    private Long idCliente;
    private String nombreCompleto;
    private String documentoIdentidad;
    private String telefono;
    private String email;
    private String direccion;

    /**
     * Constructor vacío requerido por frameworks.
     */
    public ClienteDTO() {
    }

    /**
     * Constructor completo para facilitar la creación.
     * 
     * @param id identificador único
     * @param nombre nombre completo del cliente
     * @param doc documento de identidad
     * @param tel teléfono de contacto
     * @param email correo electrónico
     * @param dir dirección física
     */
    public ClienteDTO(Long id, String nombre, String doc, 
                      String tel, String email, String dir) {
        this.idCliente = id;
        this.nombreCompleto = nombre;
        this.documentoIdentidad = doc;
        this.telefono = tel;
        this.email = email;
        this.direccion = dir;
    }

    // GETTERS Y SETTERS 

    public Long getIdCliente() { 
        return idCliente; 
    }

    public void setIdCliente(Long idCliente) { 
        this.idCliente = idCliente; 
    }

    public String getNombreCompleto() { 
        return nombreCompleto; 
    }

    public void setNombreCompleto(String nombreCompleto) { 
        this.nombreCompleto = nombreCompleto; 
    }

    public String getDocumentoIdentidad() { 
        return documentoIdentidad; 
    }

    public void setDocumentoIdentidad(String documentoIdentidad) { 
        this.documentoIdentidad = documentoIdentidad; 
    }

    public String getTelefono() { 
        return telefono; 
    }

    public void setTelefono(String telefono) { 
        this.telefono = telefono; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getDireccion() { 
        return direccion; 
    }

    public void setDireccion(String direccion) { 
        this.direccion = direccion; 
    }

    // MÉTODOS DE UTILIDAD

    @Override
    public String toString() {
        return "ClienteDTO{" +
                "idCliente=" + idCliente +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", documentoIdentidad='" + documentoIdentidad + '\'' +
                '}';
    }
}