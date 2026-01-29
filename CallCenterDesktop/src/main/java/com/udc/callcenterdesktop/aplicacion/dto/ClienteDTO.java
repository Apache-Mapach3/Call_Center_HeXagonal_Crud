package com.udc.callcenterdesktop.aplicacion.dto;

/**
 * DTO para transporte de datos de Cliente entre Vista y Aplicación.
 */
public class ClienteDTO {
    
    private Long idCliente;
    private String nombreCompleto;
    private String documentoIdentidad;
    private String telefono;
    private String email;
    private String direccion;

    public ClienteDTO() {}

    public ClienteDTO(Long id, String nombre, String doc, String tel, String email, String dir) {
        this.idCliente = id;
        this.nombreCompleto = nombre;
        this.documentoIdentidad = doc;
        this.telefono = tel;
        this.email = email;
        this.direccion = dir;
    }

    // Getters y Setters Compactos
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

    @Override
    public String toString() { 
        return nombreCompleto + " (" + documentoIdentidad + ")"; 
    }

    public void setDocumento(String text) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getDocumento() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}