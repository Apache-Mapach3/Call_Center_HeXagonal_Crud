/*
 * Sistema de Gestión de Call Center
 * Arquitectura Hexagonal (Ports & Adapters)
 * Base de Datos: SQLite
 */
package com.udc.callcenterdesktop;

// Importaciones de Servicios (Capa de Aplicación)
import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;
import com.udc.callcenterdesktop.aplicacion.servicios.CampaniaService;
import com.udc.callcenterdesktop.aplicacion.servicios.ClienteService;
import com.udc.callcenterdesktop.aplicacion.servicios.LlamadaService;

// Importaciones de Interfaces - Puertos de Entrada
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;

// Importaciones de Adaptadores - Capa de Infraestructura
import com.udc.callcenterdesktop.infraestructura.entrada.vistas.MenuPrincipal;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.AgenteMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.CampaniaMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.ClienteMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.LlamadaMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.ConexionDB;

// Importaciones de manejo de excepciones
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

// Utilidades Java
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase Principal del Sistema Call Center Desktop.
 * 
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Inicializar la infraestructura de base de datos (SQLite)</li>
 *   <li>Configurar el Look & Feel de la interfaz gráfica</li>
 *   <li>Realizar la inyección manual de dependencias (Wiring)</li>
 *   <li>Lanzar la interfaz gráfica principal</li>
 *   <li>Manejar errores críticos de inicialización</li>
 * </ul>
 * 
 * <p><b>Arquitectura:</b> Esta clase actúa como el punto de entrada y 
 * "Composition Root" del patrón de Inyección de Dependencias.</p>
 * 
 * <p><b>Tecnologías:</b></p>
 * <ul>
 *   <li>Base de Datos: SQLite (archivo local)</li>
 *   <li>GUI: Java Swing</li>
 *   <li>Patrón: Arquitectura Hexagonal</li>
 * </ul>
 * 
 * @author Sistema Call Center Team
 * @version 3.0 - SQLite Edition
 * @since 2025
 */
public class Main {
    
    // Logger para registro de eventos críticos
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    
    /**
     * Método principal de arranque del sistema.
     * 
     * <p>Flujo de inicialización:</p>
     * <ol>
     *   <li>Configurar Look & Feel visual</li>
     *   <li>Inicializar estructura de base de datos SQLite</li>
     *   <li>Crear adaptadores de persistencia</li>
     *   <li>Instanciar servicios de aplicación</li>
     *   <li>Lanzar interfaz gráfica</li>
     * </ol>
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        
        // Configurar apariencia visual (Look & Feel)
        configurarLookAndFeel();
        
        // Inicializar Base de Datos SQLite
        if (!inicializarBaseDeDatos()) {
            mostrarErrorCritico("No se pudo inicializar la base de datos SQLite.\n" +
                               "Verifique que el archivo config.properties exista y sea correcto.");
            System.exit(1); // Salir si falla la BD (crítico)
        }
        
        // INYECCIÓN DE DEPENDENCIAS (Wiring Manual)
        try {
            iniciarAplicacion();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error crítico al iniciar la aplicación", e);
            mostrarErrorCritico("Error inesperado al iniciar el sistema:\n" + 
                               e.getMessage() + 
                               "\n\nConsulte los logs para más detalles.");
            System.exit(1);
        }
    }
    
    /**
     * Configura el tema visual de la aplicación.
     * Intenta usar el Look & Feel nativo del sistema operativo.
     */
    private static void configurarLookAndFeel() {
        try {
            // Intentar usar el Look & Feel del sistema (Windows, macOS, Linux)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LOGGER.info("Look & Feel configurado: " + UIManager.getLookAndFeel().getName());
            
        } catch (Exception ex) {
            // Si falla, usar el Look & Feel por defecto de Java (Metal)
            LOGGER.log(Level.WARNING, "No se pudo aplicar Look & Feel del sistema, usando predeterminado", ex);
            
            // Intentar alternativa: Nimbus (más moderno que Metal)
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        LOGGER.info("Usando Look & Feel alternativo: Nimbus");
                        break;
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Usando Look & Feel predeterminado de Java", e);
            }
        }
    }
    
    /**
     * Inicializa la estructura de la base de datos SQLite.
     * Crea las tablas si no existen (Auto-migración).
     * 
     * @return true si la inicialización fue exitosa, false en caso contrario
     */
    private static boolean inicializarBaseDeDatos() {
        try {
            LOGGER.info("Iniciando proceso de verificación de base de datos SQLite...");
            
            // Llamar al método que crea/verifica las tablas
            ConexionDB.inicializarTablas();
            
            LOGGER.info("✓ Base de datos SQLite inicializada correctamente");
            LOGGER.info("✓ Estructura de tablas verificada/creada");
            
            return true;
            
        } catch (CallCenterException e) {
            // Error de negocio/dominio
            LOGGER.log(Level.SEVERE, "Error de dominio al inicializar BD", e);
            return false;
            
        } catch (Exception e) {
            // Error técnico inesperado
            LOGGER.log(Level.SEVERE, "Error técnico inesperado al inicializar BD", e);
            return false;
        }
    }
    
    /**
     * Realiza el wiring (cableado) de dependencias e inicia la GUI.
     * 
     * <p>Este método implementa el patrón "Composition Root",
     * donde se ensamblan todas las capas de la arquitectura hexagonal:</p>
     * <ul>
     *   <li>Adaptadores de Salida (Repositorios SQLite)</li>
     *   <li>Servicios de Aplicación (Lógica de Negocio)</li>
     *   <li>Adaptadores de Entrada (Interfaz Gráfica)</li>
     * </ul>
     */
    private static void iniciarAplicacion() {
        
        LOGGER.info("=== INICIANDO SISTEMA CALL CENTER ===");
        LOGGER.info("Arquitectura: Hexagonal (Ports & Adapters)");
        LOGGER.info("Base de Datos: SQLite");
        

        // CAPA DE INFRAESTRUCTURA - ADAPTADORES DE SALIDA (Repositorios)

        LOGGER.info("Creando adaptadores de persistencia...");
        
        AgenteMySqlAdapter repoAgentes = new AgenteMySqlAdapter();
        CampaniaMySqlAdapter repoCampanias = new CampaniaMySqlAdapter();
        ClienteMySqlAdapter repoClientes = new ClienteMySqlAdapter();
        LlamadaMySqlAdapter repoLlamadas = new LlamadaMySqlAdapter();
        
        LOGGER.info("✓ Adaptadores de persistencia creados");
        

        // CAPA DE APLICACIÓN - SERVICIOS (Lógica de Negocio)
        LOGGER.info("Instanciando servicios de aplicación...");
        
        // Servicios base (sin dependencias entre ellos)
        IAgenteService serviceAgentes = new AgenteService(repoAgentes);
        ICampaniaService serviceCampanias = new CampaniaService(repoCampanias);
        IClienteService serviceClientes = new ClienteService(repoClientes);
        
        // Servicio de Llamadas: requiere validaciones cruzadas con otros servicios
        // Se inyectan los repositorios de Agente, Cliente y Campaña para validar existencia
        ILlamadaService serviceLlamadas = new LlamadaService(
            repoLlamadas,
            repoAgentes,
            repoClientes,
            repoCampanias
        );
        
        LOGGER.info(" Servicios de aplicación instanciados con validaciones cruzadas");
        
        // CAPA DE INFRAESTRUCTURA - ADAPTADOR DE ENTRADA (GUI)
      
        LOGGER.info("Iniciando interfaz gráfica de usuario...");
        
        // Usar el Event Dispatch Thread de Swing para operaciones de GUI
        java.awt.EventQueue.invokeLater(() -> {
            try {
                // Crear e inyectar dependencias al Menú Principal
                MenuPrincipal menuPrincipal = new MenuPrincipal(
                    serviceAgentes, 
                    serviceClientes, 
                    serviceCampanias, 
                    serviceLlamadas
                );
                
                // Hacer visible la ventana principal
                menuPrincipal.setVisible(true);
                
                LOGGER.info("✓ Interfaz gráfica iniciada correctamente");
                LOGGER.info("=== SISTEMA CALL CENTER OPERATIVO ===");
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al crear la interfaz gráfica", e);
                mostrarErrorCritico("Error al iniciar la interfaz gráfica:\n" + e.getMessage());
                System.exit(1);
            }
        });
    }
    
    /**
     * Muestra un cuadro de diálogo modal con un error crítico.
     * 
     * @param mensaje mensaje de error a mostrar al usuario
     */
    private static void mostrarErrorCritico(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Error Crítico - Call Center System",
            JOptionPane.ERROR_MESSAGE
        );
    }
}