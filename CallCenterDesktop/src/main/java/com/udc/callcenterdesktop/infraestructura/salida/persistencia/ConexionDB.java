/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Clase utilitaria para gestionar la conexión JDBC a MySQL.
 * Configurada para funcionar con Laragon/XAMPP localmente.
 */
public class ConexionDB {

    // Constantes de configuración (Evitar "números mágicos" en el código)
    private static final String URL = "jdbc:mysql://localhost:3306/call_center_db?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Vacío para Laragon

    /**
     * Obtiene una nueva conexión a la base de datos.
     * @return Objeto Connection activo o null si falla.
     */
    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // Carga dinámica del driver (MySQL Connector/J)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Intento de conexión
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Log para depuración (útil en desarrollo)
            System.out.println("[ConexionDB] Conexión establecida con MySQL.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error Crítico: No se encontró el Driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error SQL: No se pudo conectar a la BD.");
            JOptionPane.showMessageDialog(null, "Error de conexión a BD: " + e.getMessage());
        }
        return conexion;
    }
}