/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO; // Necesario para el ComboBox de Agentes
import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO; // Necesario para el ComboBox de Campañas
import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO; // Necesario para el ComboBox de Clientes
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService; // Servicio de Agente
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService; // Servicio de Campaña
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService; // Servicio de Cliente
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;

/**
 * Vista: Formulario para el Registro de Interacciones de Llamada.
 * Esta vista inyecta tres servicios para manejar la data relacionada (Agente, Cliente, Campaña).
 */
public class FrmRegistroLlamada extends JFrame {

   
    // 1. DEPENDENCIAS (Puertos de Entrada)

    private final ILlamadaService llamadaService;
    private final IAgenteService agenteService;
    private final ICampaniaService campaniaService;
    private final IClienteService clienteService;

 
    // 2. COMPONENTES VISUALES
    
    // Los JComboBox deben ser declarados como campos para poder llenarlos.
    private JComboBox<String> cmbAgente; 
    private JComboBox<String> cmbCliente; 
    private JComboBox<String> cmbCampania;
    // ... otros campos como txtDuracion, txtDetalle, etc.

    /**
     * Constructor con Inyección de Múltiples Dependencias.
     */
    public FrmRegistroLlamada(ILlamadaService llamadaService, 
                              IAgenteService agenteService, 
                              ICampaniaService campaniaService,
                              IClienteService clienteService) {
        
        this.llamadaService = llamadaService;
        this.agenteService = agenteService;
        this.campaniaService = campaniaService;
        this.clienteService = clienteService;
        
   
        initComponentsPersonalizados(); 
        
        
        cargarComboBoxes();
    }
    
   
    // 3. LÓGICA DE CARGA DE DATOS PARA COMBOBOXES
    

    private void initComponentsPersonalizados() {
        // Inicializa tus componentes, incluyendo:
        // cmbAgente = new JComboBox<>();
        // cmbCliente = new JComboBox<>();
        // cmbCampania = new JComboBox<>();
        // ...
        
        // Ejemplo de inicialización de JComboBox (DEBES ADAPTAR ESTO A TU DISEÑO)
        cmbAgente = new JComboBox<>(); 
        cmbCliente = new JComboBox<>(); 
        cmbCampania = new JComboBox<>(); 
        
        // Inicializar el Layout, tamaño, etc.
        // ...
    }
    
    /**
     * Carga los datos de Agentes, Clientes y Campañas en los JComboBoxes.
     * Esto asume que los servicios tienen un método para obtener la lista de DTOs.
     */
    private void cargarComboBoxes() {
        try {
            // 1. Cargar Agentes
            List<AgenteDTO> agentes = agenteService.obtenerTodosAgentes(); // Asume este método existe
            cmbAgente.addItem("Seleccione Agente");
            for (AgenteDTO agente : agentes) {
                // Almacenamos el nombre, pero usaremos el ID para el mapeo
                cmbAgente.addItem(agente.getId() + " - " + agente.getNombre()); 
            }
            
            // 2. Cargar Clientes
            List<ClienteDTO> clientes = clienteService.obtenerTodosClientes(); // Asume este método existe
            cmbCliente.addItem("Seleccione Cliente");
            for (ClienteDTO cliente : clientes) {
                cmbCliente.addItem(cliente.getId() + " - " + cliente.getNombre());
            }

            // 3. Cargar Campañas
            List<CampaniaDTO> campanias = campaniaService.obtenerTodasCampanias(); // Ya existe
            cmbCampania.addItem("Seleccione Campaña");
            for (CampaniaDTO campania : campanias) {
                cmbCampania.addItem(campania.getId() + " - " + campania.getNombre());
            }

        } catch (CallCenterException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos base: " + e.getMessage(), "Error de Servicio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de sistema al cargar listas: " + e.getMessage(), "Error Fatal", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 
    // 4. LÓGICA DE REGISTRO (Guardar)
    // 

    /**
     * Manejador del botón Guardar (debes implementarlo en tu diseño).
     */
    private void btnRegistrarLlamadaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            LlamadaDTO dto = crearLlamadaDTODesdeFormulario();
            
            if (dto != null) {
                llamadaService.crearOActualizarLlamada(dto);
                JOptionPane.showMessageDialog(this, "Llamada registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // limpiarCampos(); 
            }
        } catch (CallCenterException e) {
            JOptionPane.showMessageDialog(this, "Error de registro: " + e.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al intentar guardar la llamada: " + e.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método auxiliar para extraer los datos del formulario a un DTO.
     */
    private LlamadaDTO crearLlamadaDTODesdeFormulario() {
        
     
        String agenteSeleccionado = (String) cmbAgente.getSelectedItem();
        String clienteSeleccionado = (String) cmbCliente.getSelectedItem();
        String campaniaSeleccionada = (String) cmbCampania.getSelectedItem();

        if (agenteSeleccionado == null || agenteSeleccionado.startsWith("Seleccione")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Agente.", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        
        Long idAgente = Long.parseLong(agenteSeleccionado.split(" - ")[0]);
        Long idCliente = Long.parseLong(clienteSeleccionado.split(" - ")[0]);
        Long idCampania = Long.parseLong(campaniaSeleccionada.split(" - ")[0]);

        LlamadaDTO dto = new LlamadaDTO();
        dto.setIdLlamada(0L); 
        dto.setFechaHora(LocalDateTime.now()); 
        
        
        dto.setIdAgente(idAgente);
        dto.setIdCliente(idCliente);
        dto.setIdCampania(idCampania);
        
        
        return dto;
    }
    
   
}