/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

/**
 * Entidad de dominio que representa un Cliente del Call Center.
 * 
 * <p>Un cliente es una persona o entidad que recibe servicios
 * o productos a través del centro de llamadas.</p>
 * 
 * <p><b>Reglas de negocio:</b></p>
 * <ul>
 *   <li>Nombre completo es obligatorio</li>
 *   <li>Documento de identidad debe ser único en el sistema</li>
 *   <li>Al menos un método de contacto (teléfono o email) es requerido</li>
 * </ul>
 * 
 * @author Carlos
 * @version 1.0
 * @since 2024
 */
public class Cliente {
    
    private Long idCliente;
    private String nombreCompleto;
    private String documentoIdentidad;
    private String telefono;
    private String email;
    private String direccion;

    /**
     * Constructor vacío requerido por frameworks.
     */
    public Cliente() {
    }

    /**
     * Constructor completo para instanciación directa.
     * 
     * @param idCliente identificador único
     * @param nombreCompleto nombre completo del cliente
     * @param documentoIdentidad cédula, DNI o identificación oficial
     * @param telefono número de teléfono principal
     * @param email correo electrónico
     * @param direccion dirección física completa
     */
    public Cliente(Long idCliente, String nombreCompleto, String documentoIdentidad, 
                   String telefono, String email, String direccion) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
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

    //MÉTODOS DE UTILIDAD 

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", documentoIdentidad='" + documentoIdentidad + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return idCliente != null && idCliente.equals(cliente.idCliente);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}