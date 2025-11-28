/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Clase de infraestructura encargada de gestionar la conexión a la base de datos SQLite.
 * * <p><b>Patrón Singleton:</b> Utiliza métodos estáticos para proveer una conexión 
 * centralizada y thread-safe.</p>
 * * <p><b>Características:</b></p>
 * <ul>
 * <li>Carga configuración externa desde 'config.properties'.</li>
 * <li>Auto-migración: Crea las tablas automáticamente si no existen.</li>
 * <li>Soporte nativo para SQLite (archivo local).</li>
 * </ul>
 * * @author Jose
 * @version 3.0 (Versión SQLite)
 */
public class ConexionDB {

    // Variables estáticas para almacenar la configuración leída del archivo
    private static String driver;
    private static String url;
    
    // Bloque estático: Se ejecuta UNA sola vez cuando la aplicación arranca.
    // Su función es leer el archivo de configuración antes de cualquier intento de conexión.
    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            
            if (input == null) {
                throw new RuntimeException("ERROR CRÍTICO: No se encontró el archivo 'config.properties' en la carpeta resources.");
            }

            Properties props = new Properties();
            props.load(input);

            // Leemos las propiedades
            driver = props.getProperty("db.driver");
            url = props.getProperty("db.url");

            // Validamos que no vengan vacías
            if (driver == null || url == null) {
                throw new RuntimeException("ERROR: El archivo config.properties está incompleto (falta driver o url).");
            }

            // Cargamos la clase del Driver en memoria
            Class.forName(driver);
            System.out.println("[ConexionDB] Driver SQLite cargado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fatal inicializando la configuración de BD: " + e.getMessage());
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos SQLite.
     * * @return Objeto Connection activo.
     * @throws CallCenterException Si ocurre un error de SQL al conectar.
     */
    public static Connection obtenerConexion() {
        try {
            // SQLite no requiere usuario ni contraseña por defecto
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error crítico de conexión a Base de Datos.");
            throw new CallCenterException("No se pudo establecer conexión con el archivo SQLite", e);
        }
    }

    /**
     * Verifica y crea la estructura de tablas necesaria para la aplicación.
     * Este método habilita la portabilidad "Plug & Play" del sistema.
     * * <p>Se ejecuta al inicio del programa (en el Main). Si el archivo .db no existe,
     * SQLite lo crea. Si las tablas no existen, este script las genera.</p>
     */
    public static void inicializarTablas() {
        // DDL (Data Definition Language) adaptado para SQLite
        // Nota: En SQLite usamos 'TEXT' en lugar de 'VARCHAR' y 'INTEGER PRIMARY KEY' autoincrementa solo.
        String[] sqlCreation = {
            // Tabla Agentes
            "CREATE TABLE IF NOT EXISTS agentes ("
                + "id_agente INTEGER PRIMARY KEY, "
                + "nombre_completo TEXT NOT NULL, "
                + "numero_empleado TEXT NOT NULL UNIQUE, "
                + "telefono_contacto TEXT, "
                + "email TEXT NOT NULL, "
                + "horario_turno TEXT, "
                + "nivel_experiencia TEXT"
            + ");",

            // Tabla Clientes
            "CREATE TABLE IF NOT EXISTS clientes ("
                + "id_cliente INTEGER PRIMARY KEY, "
                + "nombre_completo TEXT NOT NULL, "
                + "documento_identidad TEXT NOT NULL UNIQUE, "
                + "telefono TEXT NOT NULL, "
                + "email TEXT, "
                + "direccion TEXT"
            + ");",

            // Tabla Campañas
            "CREATE TABLE IF NOT EXISTS campanias ("
                + "id_campania INTEGER PRIMARY KEY, "
                + "nombre_campania TEXT NOT NULL UNIQUE, "
                + "tipo_campania TEXT, "
                + "fecha_inicio TEXT NOT NULL, " // SQLite guarda fechas como TEXT (ISO-8601)
                + "fecha_fin TEXT, "
                + "supervisores_cargo TEXT, "
                + "descripcion_objetivos TEXT"
            + ");",

            // Tabla Llamadas (Transaccional)
            "CREATE TABLE IF NOT EXISTS llamadas ("
                + "id_llamada INTEGER PRIMARY KEY, "
                + "fecha_hora TEXT, "
                + "duracion_segundos INTEGER NOT NULL, "
                + "detalle_resultado TEXT NOT NULL, "
                + "observaciones TEXT, "
                + "id_agente INTEGER NOT NULL, "
                + "id_campania INTEGER NOT NULL, "
                + "id_cliente INTEGER NOT NULL, "
                + "FOREIGN KEY(id_agente) REFERENCES agentes(id_agente), "
                + "FOREIGN KEY(id_campania) REFERENCES campanias(id_campania), "
                + "FOREIGN KEY(id_cliente) REFERENCES clientes(id_cliente)"
            + ");"
        };

        // Try-with-resources para asegurar cierre de conexión y statement
        try (Connection conn = obtenerConexion(); Statement stmt = conn.createStatement()) {
            
            // CRÍTICO: En SQLite las Foreign Keys están apagadas por defecto. Hay que prenderlas manualmente.
            stmt.execute("PRAGMA foreign_keys = ON;");
            
            // Ejecutamos cada sentencia de creación
            for (String sql : sqlCreation) {
                stmt.executeUpdate(sql);
            }
            
            System.out.println("[SQLite] Estructura de tablas verificada/creada correctamente.");
            
        } catch (SQLException e) {
            // Solo imprimimos en consola porque si falla aquí, la app probablemente no funcionará,
            // pero dejamos que el flujo continúe o se maneje en el Main.
            System.err.println("Error inicializando tablas en SQLite: " + e.getMessage());
            e.printStackTrace();
        }
    }
}