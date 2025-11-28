/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.*;
import com.udc.callcenterdesktop.dominio.puertos.entrada.*;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz gráfica para el Registro y Control de Llamadas.
 * 
 * <p>Funcionalidad Transaccional:</p>
 * <ul>
 * <li>Vincular Agente, Cliente y Campaña</li>
 * <li>Registrar duración y resultado de la interacción</li>
 * <li>Visualizar historial reciente</li>
 * </ul>
 * 
 * <p><b>Inyección Múltiple:</b> Requiere servicios de Agentes, Clientes y Campañas
 * para poblar las listas desplegables (Combos).</p>
 * 
 * @author Carlos
 * @version 2.0
 * @since 2025
 */
public class FrmRegistroLlamada extends JFrame {

    // DEPENDENCIAS (Múltiples servicios necesarios)
    private final ILlamadaService llamadaService;
    private final IAgenteService agenteService;
    private final IClienteService clienteService;
    private final ICampaniaService campaniaService;
    
    // COMPONENTES UI
    private JComboBox<ItemCombo> cbAgente;
    private JComboBox<ItemCombo> cbCliente;
    private JComboBox<ItemCombo> cbCampania;
    private JTextField txtDuracion;
    private JTextArea txtDetalle;
    private JTextArea txtObservacion;
    
    private JTable tablaLlamadas;
    private DefaultTableModel modeloTabla;
    
    // CONSTANTES UI 
    private static final Color COLOR_HEADER = new Color(102, 51, 153); // Morado para Llamadas
    private static final Color COLOR_BOTON_GUARDAR = new Color(40, 167, 69);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 12);

    /**
     * Constructor con inyección de múltiples dependencias.
     * 
     * @param ls Servicio de Llamadas
     * @param as Servicio de Agentes (para cargar combo)
     * @param cs Servicio de Clientes (para cargar combo)
     * @param camps Servicio de Campañas (para cargar combo)
     */
    public FrmRegistroLlamada(ILlamadaService ls, IAgenteService as, IClienteService cs, ICampaniaService camps) {
        if (ls == null || as == null || cs == null || camps == null) {
            throw new IllegalArgumentException("Ningún servicio inyectado puede ser null");
        }
        this.llamadaService = ls;
        this.agenteService = as;
        this.clienteService = cs;
        this.campaniaService = camps;
        
        initUI();
        cargarCombos();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Registro de Llamadas - Call Center");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(crearPanelHeader(), BorderLayout.NORTH);
        add(crearPanelCentro(), BorderLayout.CENTER);
        add(crearPanelTabla(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelHeader() {
        JPanel pHeader = new JPanel();
        pHeader.setBackground(COLOR_HEADER);
        pHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel lblTitulo = new JLabel("REGISTRO DE INTERACCIONES");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FUENTE_TITULO);
        
        pHeader.add(lblTitulo);
        return pHeader;
    }

    private JPanel crearPanelCentro() {
        JPanel pCentro = new JPanel(new BorderLayout(10, 10));
        pCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pCentro.add(crearPanelFormulario(), BorderLayout.CENTER);
        pCentro.add(crearPanelBotones(), BorderLayout.SOUTH);
        
        return pCentro;
    }

    private JPanel crearPanelFormulario() {
        JPanel pForm = new JPanel(new GridLayout(3, 4, 15, 15));
        pForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Detalles de la Llamada",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        // Inicializar componentes
        cbAgente = new JComboBox<>();
        cbCliente = new JComboBox<>();
        cbCampania = new JComboBox<>();
        txtDuracion = new JTextField();
        txtDetalle = new JTextArea(); 
        txtDetalle.setBorder(BorderFactory.createEtchedBorder());
        txtObservacion = new JTextArea(); 
        txtObservacion.setBorder(BorderFactory.createEtchedBorder());

        // Agregar componentes al formulario
        pForm.add(crearLabel("Agente Responsable:")); 
        pForm.add(cbAgente);
        pForm.add(crearLabel("Cliente Contactado:")); 
        pForm.add(cbCliente);
        
        pForm.add(crearLabel("Campaña:")); 
        pForm.add(cbCampania);
        pForm.add(crearLabel("Duración (segundos):")); 
        pForm.add(txtDuracion);
        
        pForm.add(crearLabel("Resultado:")); 
        pForm.add(new JScrollPane(txtDetalle));
        pForm.add(crearLabel("Observaciones:")); 
        pForm.add(new JScrollPane(txtObservacion));

        return pForm;
    }

    private JPanel crearPanelBotones() {
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnGuardar = crearBoton("REGISTRAR LLAMADA", COLOR_BOTON_GUARDAR, e -> guardar());
        JButton btnLimpiar = crearBoton("LIMPIAR FORMULARIO", new Color(108, 117, 125), e -> limpiar());

        pBtn.add(btnGuardar); 
        pBtn.add(btnLimpiar);
        return pBtn;
    }

    private JPanel crearPanelTabla() {
        JPanel pTabla = new JPanel(new BorderLayout());
        pTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Historial Reciente",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        String[] columnas = {"ID", "Fecha", "Agente", "Cliente", "Campaña", "Resultado"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override 
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
        
        tablaLlamadas = new JTable(modeloTabla);
        tablaLlamadas.setRowHeight(25);
        tablaLlamadas.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(tablaLlamadas);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        pTabla.add(scrollPane, BorderLayout.CENTER);
        return pTabla;
    }

    /**
     * Carga los ComboBox con datos reales de la BD usando los servicios inyectados.
     */
    private void cargarCombos() {
        try {
            // Cargar Agentes
            cbAgente.removeAllItems();
            for (AgenteDTO a : agenteService.listarAgentes()) {
                cbAgente.addItem(new ItemCombo(a.getId(), a.getNombre()));
            }

            // Cargar Clientes
            cbCliente.removeAllItems();
            for (ClienteDTO c : clienteService.listarClientes()) {
                cbCliente.addItem(new ItemCombo(c.getIdCliente(), c.getNombreCompleto()));
            }

            // Cargar Campañas
            cbCampania.removeAllItems();
            for (CampaniaDTO c : campaniaService.listarCampanias()) {
                cbCampania.addItem(new ItemCombo(c.getIdCampania(), c.getNombreCampania()));
            }
                
        } catch (Exception e) {
            mostrarError("Error al cargar listas desplegables: " + e.getMessage());
        }
    }

    /**
     * Carga el historial de llamadas en la tabla.
     */
    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            List<LlamadaDTO> llamadas = llamadaService.listarHistorial();
            
            for (LlamadaDTO l : llamadas) {
                modeloTabla.addRow(new Object[]{
                    l.getIdLlamada(),
                    l.getFechaHora(),
                    l.getNombreAgente(),
                    l.getNombreCliente(),
                    l.getNombreCampania(),
                    l.getDetalleResultado()
                });
            }
        } catch (Exception ex) {
            mostrarError("Error al cargar historial: " + ex.getMessage());
        }
    }

    /**
     * Guarda una nueva llamada en el sistema.
     * Valida los datos y llama al servicio correspondiente.
     */
    private void guardar() {
        try {
            // Crear DTO para la llamada
            LlamadaDTO dto = new LlamadaDTO();
            dto.setFechaHora(LocalDateTime.now());
            
            // Validar y obtener duración
            String duracionTexto = txtDuracion.getText().trim();
            if (duracionTexto.isEmpty()) {
                throw new CallCenterException("La duración es obligatoria.");
            }
            dto.setDuracion(Integer.parseInt(duracionTexto));
            
            // Obtener detalle y observaciones
            dto.setDetalleResultado(txtDetalle.getText());
            dto.setObservaciones(txtObservacion.getText());

            // Obtener los objetos seleccionados de los ComboBox
            ItemCombo agenteSeleccionado = (ItemCombo) cbAgente.getSelectedItem();
            ItemCombo clienteSeleccionado = (ItemCombo) cbCliente.getSelectedItem();
            ItemCombo campaniaSeleccionada = (ItemCombo) cbCampania.getSelectedItem();

            // Asignar IDs al DTO (validando que no sean nulos)
            if (agenteSeleccionado != null) {
                dto.setIdAgente(agenteSeleccionado.id);
            }
            if (clienteSeleccionado != null) {
                dto.setIdCliente(clienteSeleccionado.id);
            }
            if (campaniaSeleccionada != null) {
                dto.setIdCampania(campaniaSeleccionada.id);
            }

            // Llamar al servicio (CAPA DE APLICACIÓN)
            llamadaService.registrarLlamada(dto);
            
            // Mostrar mensaje de éxito
            mostrarExito("Llamada registrada correctamente");
            
            // Limpiar formulario y recargar tabla
            limpiar();
            cargarTabla();
            
        } catch (NumberFormatException ex) {
            mostrarError("La duración debe ser un número entero (segundos).");
        } catch (CallCenterException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado: " + ex.getMessage());
            ex.printStackTrace(); // Para debugging
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiar() {
        txtDuracion.setText("");
        txtDetalle.setText("");
        txtObservacion.setText("");
        
        if (cbAgente.getItemCount() > 0) {
            cbAgente.setSelectedIndex(0);
        }
        if (cbCliente.getItemCount() > 0) {
            cbCliente.setSelectedIndex(0);
        }
        if (cbCampania.getItemCount() > 0) {
            cbCampania.setSelectedIndex(0);
        }
    }

    // MÉTODOS DE UTILIDAD PARA CREAR COMPONENTES


    /**
     * Crea un JLabel con formato estándar.
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(FUENTE_LABEL);
        return label;
    }

    /**
     * Crea un JButton con formato estándar.
     */
    private JButton crearBoton(String texto, Color color, java.awt.event.ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(action);
        return boton;
    }

    /**
     * Muestra un mensaje de error.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de advertencia.
     */
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Muestra un mensaje de éxito.
     */
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clase auxiliar interna para manejar pares ID-Texto en los ComboBox.
     * 
     * <p>Permite almacenar el ID numérico junto con el texto descriptivo
     * que se muestra en el combo.</p>
     */
    private class ItemCombo {
        Long id;
        String texto;

        /**
         * Constructor del ItemCombo.
         * 
         * @param id identificador numérico de la entidad
         * @param texto texto descriptivo a mostrar
         */
        public ItemCombo(Long id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        /**
         * Retorna el texto a mostrar en el ComboBox.
         */
        @Override
        public String toString() {
            return texto;
        }
    }
}