package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Gestión de Conexión a MySQL - Versión KISS
 * SIMPLIFICADO: Sin scripts externos, creación directa de tablas
 */
public class ConexionDB {

    private static String url;
    private static String user;
    private static String password;

    // Cargar configuración al iniciar la clase
    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("⚠️ config.properties no encontrado, usando valores por defecto");
                url = "jdbc:mysql://localhost:3306/callcenter_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
                user = "root";
                password = "123456"; 
            } else {
                prop.load(input);
                url = prop.getProperty("db.url");
                user = prop.getProperty("db.user");
                password = prop.getProperty("db.password");
            }
            
            System.out.println("✅ Configuración de BD cargada correctamente");
            
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("❌ Error cargando configuración de base de datos");
        }
    }

    /**
     * Obtiene una conexión a la base de datos
     */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Inicializa la base de datos creando las tablas necesarias
     * KISS: Todo en un solo método, sin archivos externos
     */
    public static void inicializarBaseDeDatos() {
        System.out.println("🔄 Inicializando base de datos...");
        
        try (Connection conn = obtenerConexion();
             Statement st = conn.createStatement()) {

            // TABLA AGENTES
            st.execute("""
                CREATE TABLE IF NOT EXISTS agentes (
                    id_agente BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre_completo VARCHAR(100) NOT NULL,
                    numero_empleado VARCHAR(50) UNIQUE NOT NULL,
                    telefono_contacto VARCHAR(20),
                    email VARCHAR(100),
                    horario_turno VARCHAR(50),
                    nivel_experiencia VARCHAR(50),
                    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            // TABLA CLIENTES
            st.execute("""
                CREATE TABLE IF NOT EXISTS clientes (
                    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre_completo VARCHAR(100) NOT NULL,
                    documento_identidad VARCHAR(50) UNIQUE NOT NULL,
                    telefono VARCHAR(20),
                    email VARCHAR(100),
                    direccion VARCHAR(200),
                    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            // TABLA CAMPAÑAS
            st.execute("""
                CREATE TABLE IF NOT EXISTS campanias (
                    id_campania BIGINT AUTO_INCREMENT PRIMARY KEY,
                    nombre_campania VARCHAR(100) NOT NULL,
                    tipo_campania VARCHAR(50),
                    descripcion_objetivos TEXT,
                    fecha_inicio DATE,
                    fecha_fin DATE,
                    supervisores_cargo VARCHAR(200),
                    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            // TABLA LLAMADAS
            st.execute("""
                CREATE TABLE IF NOT EXISTS llamadas (
                    id_llamada BIGINT AUTO_INCREMENT PRIMARY KEY,
                    fecha_hora DATETIME NOT NULL,
                    duracion INT COMMENT 'Duración en segundos',
                    detalle_resultado VARCHAR(100),
                    observaciones TEXT,
                    id_agente BIGINT NOT NULL,
                    id_cliente BIGINT NOT NULL,
                    id_campania BIGINT NOT NULL,
                    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (id_agente) REFERENCES agentes(id_agente) ON DELETE RESTRICT,
                    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE RESTRICT,
                    FOREIGN KEY (id_campania) REFERENCES campanias(id_campania) ON DELETE RESTRICT
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
            """);

            System.out.println("✅ Base de datos inicializada correctamente");
            
        } catch (SQLException e) {
            // No falla si las tablas ya existen
            if (e.getMessage().contains("already exists")) {
                System.out.println("ℹ️ Las tablas ya existen, continuando...");
            } else {
                System.err.println("⚠️ Error al inicializar BD: " + e.getMessage());
            }
        }
    }
}