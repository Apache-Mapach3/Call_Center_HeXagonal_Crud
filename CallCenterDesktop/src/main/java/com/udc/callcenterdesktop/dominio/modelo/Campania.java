/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.modelo;

import java.time.LocalDate;

public class Campania {

    
    

    private int id; // Corresponde a id_campania
    private String nombre; // Corresponde a nombre_campania
    private String tipoCampania;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String supervisoresCargo;
    private String descripcionObjetivos;
    private String estado; 

    
   
   
    public Campania() {
        // Constructor vacío (necesario para mapeo JDBC)
    }

    // Constructor completo (ajusta a 8 parámetros, ID como int)
    public Campania(int id, String nombre, String tipoCampania, LocalDate fechaInicio, LocalDate fechaFin, String supervisoresCargo, String descripcionObjetivos, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.tipoCampania = tipoCampania;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.supervisoresCargo = supervisoresCargo;
        this.descripcionObjetivos = descripcionObjetivos;
        this.estado = estado;
    }

   
    // 3. GETTERS Y SETTERS (Resuelve los errores 'cannot find symbol method...')

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoCampania() {
        return tipoCampania;
    }
    public void setTipoCampania(String tipoCampania) {
        this.tipoCampania = tipoCampania;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getSupervisoresCargo() {
        return supervisoresCargo;
    }
    public void setSupervisoresCargo(String supervisoresCargo) {
        this.supervisoresCargo = supervisoresCargo;
    }

    public String getDescripcionObjetivos() {
        return descripcionObjetivos;
    }
    public void setDescripcionObjetivos(String descripcionObjetivos) {
        this.descripcionObjetivos = descripcionObjetivos;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
}