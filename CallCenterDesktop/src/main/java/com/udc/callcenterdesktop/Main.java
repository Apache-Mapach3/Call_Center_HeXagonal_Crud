package com.udc.callcenterdesktop;

import com.udc.callcenterdesktop.infraestructura.salida.persistencia.ConexionDB;
import com.udc.callcenterdesktop.infraestructura.entrada.vistas.MenuPrincipal;

import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;

import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;

import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;
import com.udc.callcenterdesktop.aplicacion.servicios.ClienteService;
import com.udc.callcenterdesktop.aplicacion.servicios.CampaniaService;
import com.udc.callcenterdesktop.aplicacion.servicios.LlamadaService;

// Probablemente estén en: infraestructura.salida.persistencia
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.AgenteMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.ClienteMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.CampaniaMySqlAdapter;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.LlamadaMySqlAdapter;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // 1. Inicializar Base de Datos
        ConexionDB.inicializarBaseDeDatos();

        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🔧 Construyendo dependencias...");

                // PASO A: Crear los Repositorios (Conexión a BD)
                IAgenteRepository agenteRepo = new AgenteMySqlAdapter();
                IClienteRepository clienteRepo = new ClienteMySqlAdapter();
                ICampaniaRepository campaniaRepo = new CampaniaMySqlAdapter();
                ILlamadaRepository llamadaRepo = new LlamadaMySqlAdapter();

                // PASO B: Crear los Servicios (Lógica de Negocio) e inyectar repos
                IAgenteService agenteService = new AgenteService(agenteRepo);
                IClienteService clienteService = new ClienteService(clienteRepo);
                ICampaniaService campaniaService = new CampaniaService(campaniaRepo);
                
                // Nota: A veces LlamadaService necesita repos de cliente/agente también. 
                // Si te da error aquí, revisa qué pide el constructor de LlamadaService.
                ILlamadaService llamadaService = (ILlamadaService) new LlamadaService(llamadaRepo); 

                System.out.println("🚀 Iniciando Ventana Principal...");

                // PASO C: Crear la Ventana inyectando los Servicios
                // Aquí es donde solucionamos el error que te salía
                MenuPrincipal ventana = new MenuPrincipal(
                        agenteService, 
                        clienteService, 
                        campaniaService, 
                        llamadaService
                );
                
                ventana.setLocationRelativeTo(null);
                ventana.setVisible(true);
                
                System.out.println("✅ Aplicación cargada correctamente.");
                
            } catch (Exception e) {
                System.err.println("❌ Error crítico al iniciar: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}