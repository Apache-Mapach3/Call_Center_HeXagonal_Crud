package com.udc.callcenterdesktop.dominio.excepciones;

/**
 * Excepción personalizada del Dominio.
 * Envuelve errores técnicos (SQL, Red) en un error de negocio entendible.
 * Hereda de RuntimeException para no ensuciar el código con 'throws' obligatorios.
 */
public class CallCenterException extends RuntimeException {

    public CallCenterException(String mensaje) {
        super(mensaje);
    }

    public CallCenterException(String mensaje, Throwable causaOriginal) {
        super(mensaje, causaOriginal);
    }
}