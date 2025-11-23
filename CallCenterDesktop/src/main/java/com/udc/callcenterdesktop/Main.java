/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.udc.callcenterdesktop;
import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import com.udc.callcenterdesktop.infraestructura.entrada.vistas.FrmGestionAgentes;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.AgenteMySqlAdapter;

public class Main {
    public static void main(String[] args) {
        
        // Estilo visual (Opcional, pero se ve mejor)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}

        // INYECCIÓN DE DEPENDENCIAS
        
        // 1. Crear el Repositorio (El que sabe SQL)
        IAgenteRepository repositorio = new AgenteMySqlAdapter();

        // 2. Crear el Servicio (El cerebro, que recibe el repositorio)
        AgenteService servicio = new AgenteService(repositorio);

        // 3. Crear la Ventana (La cara, que recibe el cerebro)
        java.awt.EventQueue.invokeLater(() -> {
            new FrmGestionAgentes(servicio).setVisible(true);
        });
    }
}