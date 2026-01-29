package com.udc.callcenterdesktop.dominio.modelo;

/**
 * Entidad Cliente - Versión KISS
 * SIMPLIFICADO: Todos los métodos funcionan correctamente
 */
public class Cliente {
    
    private Long idCliente;
    private String nombreCompleto;
    private String documentoIdentidad;
    private String telefono;
    private String email;
    private String direccion;

    // Constructor vacío
    public Cliente() {
    }

    // Constructor completo
    public Cliente(Long idCliente, String nombreCompleto, String documentoIdentidad, 
                   String telefono, String email, String direccion) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.documentoIdentidad = documentoIdentidad;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    // ===== GETTERS Y SETTERS =====

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

    // ===== ALIAS PARA COMPATIBILIDAD =====
    
    public String getDocumento() {
        return documentoIdentidad;
    }

    public void setDocumento(String documento) {
        this.documentoIdentidad = documento;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", documentoIdentidad='" + documentoIdentidad + '\'' +
                '}';
    }
}