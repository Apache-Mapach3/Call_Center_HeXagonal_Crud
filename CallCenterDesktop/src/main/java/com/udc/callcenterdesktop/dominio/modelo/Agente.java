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
 * Representa a un empleado del Call Center (Entidad de Dominio).
 * Esta clase es un POJO (Plain Old Java Object) y no debe depender de frameworks.
 */
public class Agente {

    // Atributos privados (Encapsulamiento)
    private Long idAgente;
    private String nombreCompleto;
    private String numeroEmpleado;
    private String telefonoContacto;
    private String email;
    private String horarioTurno;
    private String nivelExperiencia;


    // CONSTRUCTORES


    /**
     * Constructor vacío necesario para frameworks y creación flexible.
     */
    public Agente() {
    }

    /**
     * Constructor completo para instanciar un agente con todos sus datos.
     */
    public Agente(Long idAgente, String nombreCompleto, String numeroEmpleado, String telefonoContacto, String email, String horarioTurno, String nivelExperiencia) {
        this.idAgente = idAgente;
        this.nombreCompleto = nombreCompleto;
        this.numeroEmpleado = numeroEmpleado;
        this.telefonoContacto = telefonoContacto;
        this.email = email;
        this.horarioTurno = horarioTurno;
        this.nivelExperiencia = nivelExperiencia;
    }

 
    // GETTERS Y SETTERS (Métodos de acceso)
 

    public Long getIdAgente() { return idAgente; }
    public void setIdAgente(Long idAgente) { this.idAgente = idAgente; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHorarioTurno() { return horarioTurno; }
    public void setHorarioTurno(String horarioTurno) { this.horarioTurno = horarioTurno; }

    public String getNivelExperiencia() { return nivelExperiencia; }
    public void setNivelExperiencia(String nivelExperiencia) { this.nivelExperiencia = nivelExperiencia; }

    public void setId(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setCodigoAgente(String codigoAgente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setNombre(String nombre) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setApellido(String apellido) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setEstado(String estado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getCodigoAgente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getNombre() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getApellido() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getEstado() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}