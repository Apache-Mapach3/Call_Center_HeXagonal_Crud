package com.udc.callcenterdesktop.dominio.modelo;

/**
 * Entidad Agente - Versión KISS
 * SIMPLIFICADO: Todos los getters/setters funcionan correctamente
 */
public class Agente {

    private Long idAgente;
    private String nombreCompleto;
    private String numeroEmpleado;
    private String telefonoContacto;
    private String email;
    private String horarioTurno;
    private String nivelExperiencia;

    // Constructor vacío
    public Agente() {
    }

    // Constructor completo
    public Agente(Long idAgente, String nombreCompleto, String numeroEmpleado, 
                  String telefonoContacto, String email, String horarioTurno, 
                  String nivelExperiencia) {
        this.idAgente = idAgente;
        this.nombreCompleto = nombreCompleto;
        this.numeroEmpleado = numeroEmpleado;
        this.telefonoContacto = telefonoContacto;
        this.email = email;
        this.horarioTurno = horarioTurno;
        this.nivelExperiencia = nivelExperiencia;
    }

    // ===== GETTERS Y SETTERS PRINCIPALES =====
    
    public Long getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Long idAgente) {
        this.idAgente = idAgente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    // ===== ALIAS PARA COMPATIBILIDAD =====
    // Estos métodos delegan a los principales para mantener compatibilidad
    
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

    @Override
    public String toString() {
        return "Agente{" +
                "idAgente=" + idAgente +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", numeroEmpleado='" + numeroEmpleado + '\'' +
                '}';
    }
}