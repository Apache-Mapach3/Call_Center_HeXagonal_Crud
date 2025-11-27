/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona las conexiones a la base de datos MySQL.
 * Implementa el patrón Factory para centralizar la creación de conexiones.
 * 
 * <p>Las credenciales se cargan desde un archivo de propiedades externo
 * ubicado en <code>src/main/resources/config.properties</code> para evitar
 * hardcodear información sensible en el código fuente.</p>
 * 
 * <p><b>Uso recomendado:</b></p>
 * <pre>{@code
 * try (Connection conn = ConexionDB.obtenerConexion()) {
 *     // Operaciones con la base de datos
 * } catch (CallCenterException e) {
 *     // Manejo de errores
 * }
 * }</pre>
 * 
 * @author Jose, Carlos 
 * @version 2.0
 * @since 2025
 */
public class ConexionDB {

    private static final Properties CONFIG = new Properties();
    private static boolean configuracionCargada = false;
    
    // Bloque estático para cargar la configuración una sola vez
    static {
        cargarConfiguracion();
    }

    /**
     * Carga la configuración desde el archivo properties.
     * 
     * @throws CallCenterException si no se puede cargar la configuración
     */
    private static void cargarConfiguracion() {
        try (InputStream input = ConexionDB.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input == null) {
                throw new CallCenterException(
                    "Archivo 'config.properties' no encontrado en el classpath. " +
                    "Asegúrese de que existe en src/main/resources/"
                );
            }
            
            CONFIG.load(input);
            
            // Validar que las propiedades requeridas existan
            validarPropiedades();
            
            // Cargar el driver JDBC
            Class.forName(CONFIG.getProperty("db.driver"));
            
            configuracionCargada = true;
            System.out.println("[✓] Configuración de base de datos cargada correctamente");
            
        } catch (IOException e) {
            throw new CallCenterException(
                "Error al leer el archivo de configuración", e
            );
        } catch (ClassNotFoundException e) {
            throw new CallCenterException(
                "Driver JDBC no encontrado. Verifique que mysql-connector-j esté en el classpath", e
            );
        }
    }

    /**
     * Valida que todas las propiedades requeridas estén presentes.
     */
    private static void validarPropiedades() {
        String[] propiedadesRequeridas = {"db.url", "db.username", "db.password", "db.driver"};
        
        for (String propiedad : propiedadesRequeridas) {
            if (!CONFIG.containsKey(propiedad)) {
                throw new CallCenterException(
                    "Propiedad requerida '" + propiedad + "' no encontrada en config.properties"
                );
            }
        }
    }

    /**
     * Establece y retorna una conexión activa a la base de datos.
     * 
     * <p>Este método debe usarse con try-with-resources para asegurar
     * que la conexión se cierre automáticamente:</p>
     * 
     * <pre>{@code
     * try (Connection conn = ConexionDB.obtenerConexion()) {
     *     // usar la conexión
     * }
     * }</pre>
     * 
     * @return objeto Connection activo y listo para usar
     * @throws CallCenterException si no se puede establecer la conexión
     */
    public static Connection obtenerConexion() {
        if (!configuracionCargada) {
            throw new CallCenterException(
                "La configuración de base de datos no está cargada"
            );
        }
        
        try {
            Connection conexion = DriverManager.getConnection(
                CONFIG.getProperty("db.url"),
                CONFIG.getProperty("db.username"),
                CONFIG.getProperty("db.password")
            );
            
            // Configurar propiedades de la conexión
            conexion.setAutoCommit(true);
            
            return conexion;
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al conectar con la base de datos. " +
                "Verifique que MySQL esté activo y las credenciales sean correctas. " +
                "Detalle: " + e.getMessage(), e
            );
        }
    }
    
    /**
     * Cierra de forma segura una conexión.
     * Útil para cerrar conexiones en bloques finally cuando no se usa try-with-resources.
     * 
     * @param conexion la conexión a cerrar (puede ser null)
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("[!] Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Verifica si la conexión a la base de datos está disponible.
     * Útil para health checks o diagnósticos.
     * 
     * @return true si se puede conectar exitosamente, false en caso contrario
     */
    public static boolean verificarConexion() {
        try (Connection conn = obtenerConexion()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("[!] Verificación de conexión falló: " + e.getMessage());
            return false;
        }
    }
}