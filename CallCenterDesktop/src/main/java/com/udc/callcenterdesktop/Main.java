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
        try {
            // Poner estilo Windows
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}

        // 1. Crear Repositorio
        IAgenteRepository repositorio = new AgenteMySqlAdapter();

        // 2. Crear Servicio
        AgenteService servicio = new AgenteService(repositorio);

        // 3. INYECTAR EL SERVICIO A LA VENTANA (¡ESTA ES LA CLAVE!)
        java.awt.EventQueue.invokeLater(() -> {
            new FrmGestionAgentes(servicio).setVisible(true);
        });
    }
}