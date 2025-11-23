/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

/**
 *
 * @author Admin
 */


import java.time.LocalDateTime;
import java.util.Objects;



public class Llamada {
    
    private int idAgente;
    private int idCliente;

    private LocalDateTime horaInicio; 
    private LocalDateTime horaFin;    
    private int duracionSegundos;




private String resultado;     
    private String notas;          
    private String estado;
    private int id;

public Llamada() {
    }


public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(int idAgente) {
        this.idAgente = idAgente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }



public String toString() {
        return "Llamada{" +
                "id=" + id +
                ", idAgente=" + idAgente +
                ", idCliente=" + idCliente +
                ", horaInicio=" + horaInicio +
                ", duracionSegundos=" + duracionSegundos +
                ", estado='" + estado + '\'' +
                '}';
    }


public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Llamada llamada = (Llamada) o;
        return id == llamada.id;

}
public int hashCode() {
        return Objects.hash(id);
    }

    
}
