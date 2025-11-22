/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.udc.callcenterdesktop;

//  CAPA DE APLICACIÓN (Lógica) 
import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;

//  CAPA DE DOMINIO (Puertos / Contratos) 
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;

// CAPA DE INFRAESTRUCTURA (Vistas y Base de Datos) 
import com.udc.callcenterdesktop.infraestructura.entrada.vistas.FrmGestionAgentes;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.AgenteMySqlAdapter;

/**
 * Clase Principal.
 * Configura la Inyección de Dependencias manual y lanza la aplicación.
 */
public class Main {

    public static void main(String[] args) {
        
        // Configuración visual para que se vea como el sistema operativo
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) { // O "Nimbus"
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar el tema visual.");
        }

      
        // INYECCIÓN DE DEPENDENCIAS (Arquitectura Hexagonal)
        
        
        // Crea la Infraestructura de Salida (Repositorio)
        IAgenteRepository repositorio = new AgenteMySqlAdapter();

        // Crea la Aplicación (Servicio) inyectándole el Repositorio
        IAgenteService servicio = new AgenteService(repositorio);

        // Iniciar la Infraestructura de Entrada (Vista GUI)
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // OJO: Tu FrmGestionAgentes debe tener un constructor o método 
            // para recibir el 'servicio' si quieres usarlo de verdad.
            // Por ahora lo instanciamos por defecto:
            new FrmGestionAgentes().setVisible(true);
        });
    }
}