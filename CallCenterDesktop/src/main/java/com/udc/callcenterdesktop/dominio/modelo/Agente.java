package com.udc.callcenterdesktop.dominio.modelo;

public class Agente {

    private Long id;
    private String numeroEmpleado;
    private String telefonoContacto;
    private String horarioTurno;
    private String nivelExperiencia;

    // ===== CONSTRUCTORES =====

    public Agente() {
    }

    public Agente(Long id, String numeroEmpleado, String telefonoContacto,
                  String horarioTurno, String nivelExperiencia) {
        this.id = id;
        this.numeroEmpleado = numeroEmpleado;
        this.telefonoContacto = telefonoContacto;
        this.horarioTurno = horarioTurno;
        this.nivelExperiencia = nivelExperiencia;
    }

    // ===== GETTERS Y SETTERS BASE =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getHorarioTurno() {
        return horarioTurno;
    }

    public void setHorarioTurno(String horarioTurno) {
        this.horarioTurno = horarioTurno;
    }

    public String getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(String nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    // ===== MÉTODOS COMPATIBLES (ALIAS) =====
    // Estos existen SOLO para que el resto del proyecto compile

    public String getCodigoEmpleado() {
        return numeroEmpleado;
    }

    public void setCodigoEmpleado(String codigoEmpleado) {
        this.numeroEmpleado = codigoEmpleado;
    }

    public String getTelefono() {
        return telefonoContacto;
    }

    public void setTelefono(String telefono) {
        this.telefonoContacto = telefono;
    }

    public String getTurno() {
        return horarioTurno;
    }

    public void setTurno(String turno) {
        this.horarioTurno = turno;
    }

    public String getExperiencia() {
        return nivelExperiencia;
    }

    public void setExperiencia(String experiencia) {
        this.nivelExperiencia = experiencia;
    }

    public void setIdAgente(long aLong) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setEmail(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setNombreCompleto(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getEmail() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getNombreCompleto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public long getIdAgente() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
