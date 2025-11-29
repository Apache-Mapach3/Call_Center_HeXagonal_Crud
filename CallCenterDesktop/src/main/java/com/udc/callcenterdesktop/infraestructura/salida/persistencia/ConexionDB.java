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
 * Clase de infraestructura para MySQL con AUTO-CREACIÓN DE BASE DE DATOS.
 * 
 * <p><b>CARACTERÍSTICA PRINCIPAL:</b> El usuario final NO necesita crear 
 * manualmente la base de datos en MySQL Workbench. El sistema la crea 
 * automáticamente si no existe.</p>
 * 
 * <p><b>Requisitos:</b></p>
 * <ul>
 *   <li>MySQL Server instalado y ejecutándose</li>
 *   <li>Credenciales con permiso CREATE DATABASE</li>
 *   <li>Configuración correcta en config.properties</li>
 * </ul>
 * 
 * @author Jose
 * @version 5.0 - Auto-configuración inteligente
 */
public class ConexionDB {

    private static String driver;
    private static String fullUrl;      // URL completa con nombre de BD
    private static String serverUrl;    // URL solo del servidor (sin BD específica)
    private static String dbName;       // Nombre de la base de datos
    private static String user;
    private static String password;

    /**
     * Bloque estático: Carga configuración al iniciar la aplicación.
     */
    static {
        try (InputStream input = ConexionDB.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                throw new RuntimeException(
                    "ERROR CRÍTICO: No se encontró el archivo 'config.properties' " +
                    "en src/main/resources/"
                );
            }

            Properties props = new Properties();
            props.load(input);

            // Cargar propiedades
            driver = props.getProperty("db.driver");
            fullUrl = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            
            // Validar propiedades obligatorias
            if (driver == null || fullUrl == null || user == null || password == null) {
                throw new RuntimeException(
                    "ERROR: config.properties incompleto. " +
                    "Faltan: driver, url, user o password"
                );
            }
            
            // Extraer nombre de la base de datos de la URL
            // Ejemplo: jdbc:mysql://localhost:3306/callcenter_db?params
            //                                      ↑ Extraer esto
            dbName = extraerNombreBaseDatos(fullUrl);
            
            // Construir URL del servidor (sin nombre de BD específica)
            serverUrl = construirServerUrl(fullUrl);

            // Cargar el driver de MySQL
            Class.forName(driver);
            System.out.println("[ConexionDB] ✓ Driver MySQL cargado correctamente.");
            System.out.println("[ConexionDB] ✓ Configuración leída de config.properties");
            System.out.println("[ConexionDB] → Base de datos objetivo: " + dbName);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "Error: No se pudo cargar el driver de MySQL. " +
                "Verifique que mysql-connector-j esté en pom.xml", e
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                "Error fatal al inicializar configuración de BD: " + e.getMessage(), e
            );
        }
    }

    /**
     * Obtiene una conexión a la base de datos MySQL.
     * 
     * <p><b>MAGIA AUTOMÁTICA:</b> Si la base de datos no existe, 
     * este método la crea automáticamente y luego se conecta.</p>
     * 
     * @return conexión activa a MySQL
     * @throws CallCenterException si no se puede conectar después de intentar crear la BD
     */
    public static Connection obtenerConexion() {
        try {
            // Intento normal de conexión
            return DriverManager.getConnection(fullUrl, user, password);
            
        } catch (SQLException e) {
            // Detectar si el error es "base de datos no encontrada"
            boolean esErrorBaseDatosNoExiste = 
                e.getErrorCode() == 1049 || // Código MySQL: Unknown database
                e.getMessage().contains("Unknown database");
            
            if (esErrorBaseDatosNoExiste) {
                System.out.println("[MySQL] ⚠ Base de datos '" + dbName + "' no existe.");
                System.out.println("[MySQL] → Iniciando creación automática...");
                
                // Intentar crear la base de datos
                boolean creacionExitosa = crearBaseDatosSiNoExiste();
                
                if (creacionExitosa) {
                    // Reintentar conexión después de crear la BD
                    try {
                        Connection conn = DriverManager.getConnection(fullUrl, user, password);
                        System.out.println("[MySQL] ✓ Conexión exitosa a la nueva base de datos.");
                        return conn;
                        
                    } catch (SQLException ex2) {
                        throw new CallCenterException(
                            "Error crítico: Se creó la BD pero falló la reconexión.", ex2
                        );
                    }
                } else {
                    throw new CallCenterException(
                        "No se pudo crear la base de datos automáticamente. " +
                        "Créela manualmente en MySQL Workbench."
                    );
                }
            }
            
            // Si el error no es "BD no existe", lanzar el error original
            String mensaje = "Error al conectar a MySQL Server.\n\n" +
                           "Verifique:\n" +
                           "1. Que MySQL Server esté ejecutándose\n" +
                           "2. Que las credenciales sean correctas\n" +
                           "3. Que el usuario tenga permisos\n\n" +
                           "Error técnico: " + e.getMessage();
            
            JOptionPane.showMessageDialog(null, mensaje, 
                "Error de Conexión MySQL", JOptionPane.ERROR_MESSAGE);
            
            throw new CallCenterException("No se pudo conectar a MySQL Server", e);
        }
    }

    /**
     * Crea la base de datos automáticamente si no existe.
     * 
     * <p>Se conecta al servidor MySQL SIN especificar una base de datos,
     * ejecuta CREATE DATABASE y retorna el resultado.</p>
     * 
     * @return true si la creación fue exitosa, false en caso contrario
     */
    private static boolean crearBaseDatosSiNoExiste() {
        try (Connection rootConn = DriverManager.getConnection(serverUrl, user, password);
             Statement stmt = rootConn.createStatement()) {
            
            // Crear la base de datos con charset UTF8MB4
            String sqlCreate = String.format(
                "CREATE DATABASE IF NOT EXISTS %s " +
                "CHARACTER SET utf8mb4 " +
                "COLLATE utf8mb4_unicode_ci;",
                dbName
            );
            
            stmt.executeUpdate(sqlCreate);
            System.out.println("[MySQL] ✓ Base de datos '" + dbName + "' creada exitosamente.");
            
            return true;
            
        } catch (SQLException e) {
            System.err.println("[MySQL] ✗ Error al crear la base de datos automáticamente.");
            System.err.println("[MySQL] Detalles: " + e.getMessage());
            
            // Verificar si es un problema de permisos
            if (e.getErrorCode() == 1044) { // Access denied for user
                JOptionPane.showMessageDialog(null, 
                    "El usuario '" + user + "' no tiene permisos para crear bases de datos.\n\n" +
                    "Opciones:\n" +
                    "1. Use un usuario con permisos (ej: root)\n" +
                    "2. Cree manualmente la BD en Workbench:\n" +
                    "   CREATE DATABASE callcenter_db CHARACTER SET utf8mb4;\n\n" +
                    "Error técnico: " + e.getMessage(),
                    "Permisos Insuficientes", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No se pudo crear la base de datos automáticamente.\n\n" +
                    "Por favor, cree manualmente en MySQL Workbench:\n" +
                    "CREATE DATABASE callcenter_db CHARACTER SET utf8mb4;\n\n" +
                    "Error técnico: " + e.getMessage(),
                    "Error de Auto-Configuración", JOptionPane.ERROR_MESSAGE);
            }
            
            return false;
        }
    }

    /**
     * Verifica y crea la estructura de tablas.
     * 
     * <p>Este método se ejecuta después de asegurar que la BD existe.</p>
     */
    public static void inicializarTablas() {
        try (Connection conn = obtenerConexion(); 
             Statement stmt = conn.createStatement()) {
            
            System.out.println("[MySQL] Verificando/creando estructura de tablas...");
            
            // Crear tablas con formato legible
            String[] sqlCreation = {
                // Tabla Agentes
                "CREATE TABLE IF NOT EXISTS agentes (" +
                "  id_agente BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "  nombre_completo VARCHAR(100) NOT NULL, " +
                "  numero_empleado VARCHAR(20) NOT NULL UNIQUE, " +
                "  telefono_contacto VARCHAR(20), " +
                "  email VARCHAR(100) NOT NULL, " +
                "  horario_turno VARCHAR(50), " +
                "  nivel_experiencia VARCHAR(50), " +
                "  INDEX idx_numero_empleado (numero_empleado)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
                
                // Tabla Clientes
                "CREATE TABLE IF NOT EXISTS clientes (" +
                "  id_cliente BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "  nombre_completo VARCHAR(100) NOT NULL, " +
                "  documento_identidad VARCHAR(20) NOT NULL UNIQUE, " +
                "  telefono VARCHAR(20) NOT NULL, " +
                "  email VARCHAR(100), " +
                "  direccion VARCHAR(200), " +
                "  INDEX idx_documento (documento_identidad)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
                
                // Tabla Campañas
                "CREATE TABLE IF NOT EXISTS campanias (" +
                "  id_campania BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "  nombre_campania VARCHAR(100) NOT NULL UNIQUE, " +
                "  tipo_campania VARCHAR(50), " +
                "  fecha_inicio DATE NOT NULL, " +
                "  fecha_fin DATE, " +
                "  supervisores_cargo VARCHAR(200), " +
                "  descripcion_objetivos TEXT, " +
                "  INDEX idx_nombre_campania (nombre_campania)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;",
                
                // Tabla Llamadas (con Foreign Keys)
                "CREATE TABLE IF NOT EXISTS llamadas (" +
                "  id_llamada BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                "  fecha_hora DATETIME NOT NULL, " +
                "  duracion_segundos INT NOT NULL, " +
                "  detalle_resultado VARCHAR(500) NOT NULL, " +
                "  observaciones TEXT, " +
                "  id_agente BIGINT NOT NULL, " +
                "  id_campania BIGINT NOT NULL, " +
                "  id_cliente BIGINT NOT NULL, " +
                "  INDEX idx_fecha (fecha_hora), " +
                "  INDEX idx_agente (id_agente), " +
                "  INDEX idx_cliente (id_cliente), " +
                "  INDEX idx_campania (id_campania), " +
                "  CONSTRAINT fk_llamada_agente FOREIGN KEY (id_agente) " +
                "    REFERENCES agentes(id_agente) ON DELETE RESTRICT, " +
                "  CONSTRAINT fk_llamada_campania FOREIGN KEY (id_campania) " +
                "    REFERENCES campanias(id_campania) ON DELETE RESTRICT, " +
                "  CONSTRAINT fk_llamada_cliente FOREIGN KEY (id_cliente) " +
                "    REFERENCES clientes(id_cliente) ON DELETE RESTRICT" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;"
            };

            // Ejecutar cada sentencia de creación
            for (String sql : sqlCreation) {
                stmt.executeUpdate(sql);
            }
            
            System.out.println("[MySQL] ✓ Estructura de tablas verificada/creada correctamente.");
            System.out.println("[MySQL] ✓ Foreign Keys configuradas y activas.");

        } catch (SQLException e) {
            System.err.println("[MySQL] ✗ Error al inicializar tablas: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null, 
                "Error al crear la estructura de tablas.\n\n" +
                "Verifique que el usuario tenga permisos CREATE TABLE.\n\n" +
                "Error técnico: " + e.getMessage(),
                "Error de Inicialización",
                JOptionPane.ERROR_MESSAGE);
            
            throw new CallCenterException(
                "Error crítico al inicializar estructura de tablas", e
            );
        }
    }
    
    
    // MÉTODOS DE UTILIDAD PRIVADOS
   
    
    /**
     * Extrae el nombre de la base de datos de la URL JDBC.
     * 
     * @param url URL JDBC completa
     * @return nombre de la base de datos
     */
    private static String extraerNombreBaseDatos(String url) {
        try {
            // Formato: jdbc:mysql://host:port/DATABASE?params
            // Buscamos lo que está entre el último "/" y el "?" o final
            int startIndex = url.lastIndexOf("/") + 1;
            int endIndex = url.contains("?") ? url.indexOf("?") : url.length();
            
            String nombre = url.substring(startIndex, endIndex);
            
            if (nombre.isEmpty()) {
                return "callcenter_db"; // Fallback por defecto
            }
            
            return nombre;
            
        } catch (Exception e) {
            System.err.println("Error extrayendo nombre de BD, usando 'callcenter_db' por defecto");
            return "callcenter_db";
        }
    }
    
    /**
     * Construye la URL del servidor sin especificar base de datos.
     * 
     * @param fullUrl URL completa con nombre de BD
     * @return URL del servidor sin BD específica
     */
    private static String construirServerUrl(String fullUrl) {
        try {
            // Cortamos antes del nombre de la base de datos
            // jdbc:mysql://localhost:3306/callcenter_db?params
            //                            ↑ Cortamos aquí
            int lastSlash = fullUrl.lastIndexOf("/");
            String serverPart = fullUrl.substring(0, lastSlash + 1); // Incluye el "/"
            
            // Si hay parámetros, los agregamos
            String params = "";
            if (fullUrl.contains("?")) {
                params = fullUrl.substring(fullUrl.indexOf("?"));
            }
            
            return serverPart + params;
            
        } catch (Exception e) {
            System.err.println("Error construyendo URL servidor, usando fallback");
            return "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
        }
    }
    
    /**
     * Cierra una conexión de forma segura.
     * 
     * @param conn conexión a cerrar (puede ser null)
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
