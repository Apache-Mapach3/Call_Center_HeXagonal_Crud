/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.excepciones;

/**
 *
 * @author camolano
 */
/**
 * Excepción genérica de la aplicación Call Center.
 * Permite manejar errores de negocio o infraestructura de forma centralizada.
 */

public class CallCenterException extends RuntimeException {
    
    public CallCenterException(String mensaje) {
        super(mensaje);
    }

    public CallCenterException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}  
    
    
    
    

