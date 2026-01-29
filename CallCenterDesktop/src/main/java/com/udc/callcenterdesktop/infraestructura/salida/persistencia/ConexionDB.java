package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class ConexionDB {

    private static String url;
    private static String user;
    private static String password;

    // 1. CARGA DE CONFIGURACIÓN (Static Block)
    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("⚠️ No se encontró config.properties, usando valores por defecto.");
                // Fallback por si falla el archivo
                url = "jdbc:mysql://localhost:3306/callcenter_db?allowPublicKeyRetrieval=true&useSSL=false";
                user = "root";
                password = ""; 
            } else {
                prop.load(input);
                url = prop.getProperty("db.url");
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error cargando configuración de base de datos");
        }
    }

    // 2. OBTENER CONEXIÓN
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // 3. MÉTODO QUE FALTABA: inicializarBaseDeDatos
    // Este método es llamado por Main.java
    public static void inicializarBaseDeDatos() {
        // Intentamos llamar al DbInitializer si existe, o ejecutamos scripts básicos
        try {
            System.out.println("🔄 Inicializando base de datos...");
            
            // Aquí solemos llamar a los scripts principales
            // Si tienes un archivo schema.sql en resources, esto lo ejecutará
            ejecutarScript("schema.sql"); 
            
            // Si tienes datos de prueba
            // ejecutarScript("data.sql"); 
            
            System.out.println("✅ Base de datos inicializada.");
        } catch (Exception e) {
            // No lanzamos error fatal aquí para que la app arranque aunque el script falle
            // (por ejemplo, si las tablas ya existen)
            System.out.println("ℹ️ Nota: La inicialización de BD tuvo detalles o ya existía: " + e.getMessage());
        }
    }

    // 4. MÉTODO QUE FALTABA: ejecutarScript
    // Este método es usado por DbInitializer.java
    public static void ejecutarScript(String rutaScript) {
        try (Connection conn = obtenerConexion();
             InputStream is = ConexionDB.class.getClassLoader().getResourceAsStream(rutaScript)) {

            if (is == null) {
                System.out.println("⚠️ No se encontró el script: " + rutaScript);
                return;
            }

            // Usamos Scanner para leer el archivo SQL delimitado por ";"
            try (Scanner scanner = new Scanner(is)) {
                scanner.useDelimiter(";");
                
                try (Statement st = conn.createStatement()) {
                    while (scanner.hasNext()) {
                        String sql = scanner.next().trim();
                        // Ignoramos líneas vacías o comentarios simples
                        if (!sql.isEmpty() && !sql.startsWith("--")) {
                            try {
                                st.execute(sql);
                            } catch (SQLException e) {
                                // Ignoramos errores de "Tabla ya existe" para no detener el flujo
                                if (!e.getMessage().contains("already exists")) {
                                    System.err.println("Error ejecutando SQL: " + sql + " -> " + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException | SQLException e) {
            System.err.println("❌ Error crítico ejecutando script " + rutaScript + ": " + e.getMessage());
        }
    }
}