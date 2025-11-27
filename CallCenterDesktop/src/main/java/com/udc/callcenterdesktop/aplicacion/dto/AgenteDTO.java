/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.dto;

/**
 * Data Transfer Object para la entidad Agente.
 * 
 * <p>Este DTO se utiliza para transportar datos entre la capa de
 * presentación y la capa de aplicación, evitando exponer directamente
 * las entidades de dominio.</p>
 * 
 * <p><b>Ventajas del uso de DTOs:</b></p>
 * <ul>
 *   <li>Desacoplamiento entre capas</li>
 *   <li>Control de qué datos se exponen</li>
 *   <li>Facilita la serialización y deserialización</li>
 *   <li>Permite agregar campos calculados sin modificar el dominio</li>
 * </ul>
 * 
 * @author Jose 
 * @version 1.0
 * @since 2025
 */
public class AgenteDTO {
    
    private Long id;
    private String nombre;
    private String numeroEmpleado;
    private String telefono;
    private String email;
    private String turno;
    private String experiencia;

    /**
     * Constructor vacío requerido para frameworks de serialización.
     */
    public AgenteDTO() {
    }

    /**
     * Constructor completo para facilitar la creación de instancias.
     * 
     * @param id identificador único del agente
     * @param nombre nombre completo
     * @param numeroEmpleado código de empleado
     * @param telefono teléfono de contacto
     * @param email correo electrónico
     * @param turno horario de trabajo
     * @param experiencia nivel de experiencia
     */
    public AgenteDTO(Long id, String nombre, String numeroEmpleado, 
                     String telefono, String email, String turno, String experiencia) {
        this.id = id;
        this.nombre = nombre;
        this.numeroEmpleado = numeroEmpleado;
        this.telefono = telefono;
        this.email = email;
        this.turno = turno;
        this.experiencia = experiencia;
    }

    // GETTERS Y SETTERS 

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getNumeroEmpleado() { 
        return numeroEmpleado; 
    }

    public void setNumeroEmpleado(String numeroEmpleado) { 
        this.numeroEmpleado = numeroEmpleado; 
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

    public String getTurno() { 
        return turno; 
    }

    public void setTurno(String turno) { 
        this.turno = turno; 
    }

    public String getExperiencia() { 
        return experiencia; 
    }

    public void setExperiencia(String experiencia) { 
        this.experiencia = experiencia; 
    }

    // MÉTODOS DE UTILIDAD

    @Override
    public String toString() {
        return "AgenteDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numeroEmpleado='" + numeroEmpleado + '\'' +
                ", turno='" + turno + '\'' +
                '}';
    }
}