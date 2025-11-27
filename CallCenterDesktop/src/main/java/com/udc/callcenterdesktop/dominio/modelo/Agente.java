/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

/**
 * Entidad de dominio que representa un Agente del Call Center.
 * 
 * <p>Un agente es un empleado que atiende llamadas y gestiona
 * interacciones con clientes. Esta clase es un POJO puro sin
 * dependencias de frameworks externos.</p>
 * 
 * <p><b>Invariantes de negocio:</b></p>
 * <ul>
 *   <li>El nombre completo es obligatorio</li>
 *   <li>El número de empleado debe ser único</li>
 *   <li>El email debe tener formato válido si se proporciona</li>
 * </ul>
 * 
 * @author Jose 
 * @version 1.0
 * @since 2025
 */
public class Agente {
    
    private Long idAgente;
    private String nombreCompleto;
    private String numeroEmpleado;
    private String telefonoContacto;
    private String email;
    private String horarioTurno;
    private String nivelExperiencia;

    /**
     * Constructor vacío requerido por frameworks y librerías de persistencia.
     */
    public Agente() {
    }

    /**
     * Constructor completo para crear una instancia de Agente.
     * 
     * @param idAgente identificador único (puede ser null para nuevos registros)
     * @param nombreCompleto nombre completo del agente
     * @param numeroEmpleado código único de empleado
     * @param telefonoContacto número de teléfono de contacto
     * @param email correo electrónico corporativo
     * @param horarioTurno turno asignado (Mañana, Tarde, Noche)
     * @param nivelExperiencia nivel de experiencia (Junior, Intermedio, Senior)
     */
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

    //GETTERS Y SETTERS

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

    //MÉTODOS DE UTILIDAD

    @Override
    public String toString() {
        return "Agente{" +
                "idAgente=" + idAgente +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", numeroEmpleado='" + numeroEmpleado + '\'' +
                ", nivelExperiencia='" + nivelExperiencia + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agente agente = (Agente) o;
        return idAgente != null && idAgente.equals(agente.idAgente);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}