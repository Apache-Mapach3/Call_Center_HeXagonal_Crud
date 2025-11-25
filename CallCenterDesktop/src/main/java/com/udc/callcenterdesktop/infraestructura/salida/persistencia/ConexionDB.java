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
 * Implementa el patrón Singleton (conexión estática) para eficiencia.
 */
public class ConexionDB {

    // URL de Conexión
    // El parámetro serverTimezone=UTC evita errores de zona horaria en versiones nuevas de MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/callcenter_db?serverTimezone=UTC";
    
    // Credenciales (Usuario root y clave vacía por defecto en Laragon)
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    /**
     * Intenta establecer conexión con la base de datos.
     * @return Objeto Connection activo o null si falla.
     */
    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            // Cargar el Driver en memoria 
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer el puente
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Log de depuración (Solo para consola de desarrollo)
            System.out.println("[ConexionDB] Conexión establecida con MySQL.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error Crítico: No se encontró el Driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error SQL: No se pudo conectar a la BD.");
            // Alerta visual en caso de fallo crítico
            JOptionPane.showMessageDialog(null, "Error de conexión a BD: " + e.getMessage());
        }
        return conexion;
    }
}