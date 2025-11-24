/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.udc.callcenterdesktop;

// Importaciones de Servicios (Aplicación)
import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;
import com.udc.callcenterdesktop.aplicacion.servicios.CampaniaService;
import com.udc.callcenterdesktop.aplicacion.servicios.ClienteService;
import com.udc.callcenterdesktop.aplicacion.servicios.LlamadaService;

// Importaciones de Interfaces (Puertos)
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;

// Importaciones de Adaptadores (Infraestructura)
import com.udc.callcenterdesktop.infraestructura.entrada.vistas.MenuPrincipal;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.AgenteMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.CampaniaMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.ClienteMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.LlamadaMySqlAdapter;

// Importaciones de Utilidades de Java
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        
        // Configuración Visual (Look & Feel estilo Windows)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar el tema visual.");
        }

 
        // INYECCIÓN DE DEPENDENCIAS (Wiring)
     

        // Crear los Adaptadores (Repositorios que saben SQL)
        AgenteMySqlAdapter repoAgentes = new AgenteMySqlAdapter();
        CampaniaMySqlAdapter repoCampanias = new CampaniaMySqlAdapter();
        ClienteMySqlAdapter repoClientes = new ClienteMySqlAdapter();
        LlamadaMySqlAdapter repoLlamadas = new LlamadaMySqlAdapter();

        // Crear los Servicios (Lógica que usa los repositorios)
        IAgenteService serviceAgentes = new AgenteService(repoAgentes);
        ICampaniaService serviceCampanias = new CampaniaService(repoCampanias);
        IClienteService serviceClientes = new ClienteService(repoClientes);
        ILlamadaService serviceLlamadas = new LlamadaService(repoLlamadas);

        // Iniciar la GUI (Inyectando los servicios al Menú Principal)
        java.awt.EventQueue.invokeLater(() -> {
            new MenuPrincipal(serviceAgentes, serviceClientes, serviceCampanias, serviceLlamadas).setVisible(true);
        });
    }
}