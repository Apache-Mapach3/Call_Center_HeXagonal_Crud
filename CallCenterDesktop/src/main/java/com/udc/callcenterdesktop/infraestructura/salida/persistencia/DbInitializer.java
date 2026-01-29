package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

public class DbInitializer {
    public static void inicializar() {
        // Usamos el método ejecutarScript que creamos en la nueva ConexionDB
        ConexionDB.ejecutarScript("CREATE TABLE IF NOT EXISTS agentes (...);");
        ConexionDB.ejecutarScript("CREATE TABLE IF NOT EXISTS clientes (...);");
        ConexionDB.ejecutarScript("CREATE TABLE IF NOT EXISTS campanias (...);");
        ConexionDB.ejecutarScript("CREATE TABLE IF NOT EXISTS llamadas (...);");
        System.out.println("✓ Base de datos verificada.");
    }
}