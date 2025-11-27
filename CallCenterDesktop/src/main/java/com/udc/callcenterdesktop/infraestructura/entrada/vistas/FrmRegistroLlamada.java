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
 * * <p>Funcionalidad Transaccional:</p>
 * <ul>
 * <li>Vincular Agente, Cliente y Campaña</li>
 * <li>Registrar duración y resultado de la interacción</li>
 * <li>Visualizar historial reciente</li>
 * </ul>
 * * <p><b>Inyección Múltiple:</b> Requiere servicios de Agentes, Clientes y Campañas
 * para poblar los listas desplegables (Combos).</p>
 * * @author Carlos
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
     * * @param ls Servicio de Llamadas
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

        // Inicializar
        cbAgente = new JComboBox<>();
        cbCliente = new JComboBox<>();
        cbCampania = new JComboBox<>();
        txtDuracion = new JTextField();
        txtDetalle = new JTextArea(); txtDetalle.setBorder(BorderFactory.createEtchedBorder());
        txtObservacion = new JTextArea(); txtObservacion.setBorder(BorderFactory.createEtchedBorder());

       
        pForm.add(crearLabel("Agente Responsable:")); pForm.add(cbAgente);
        pForm.add(crearLabel("Cliente Contactado:")); pForm.add(cbCliente);
        
      
        pForm.add(crearLabel("Campaña:")); pForm.add(cbCampania);
        pForm.add(crearLabel("Duración (segundos):")); pForm.add(txtDuracion);
        
        
        pForm.add(crearLabel("Resultado:")); pForm.add(new JScrollPane(txtDetalle));
        pForm.add(crearLabel("Observaciones:")); pForm.add(new JScrollPane(txtObservacion));

        return pForm;
    }

    private JPanel crearPanelBotones() {
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnGuardar = crearBoton("REGISTRAR LLAMADA", COLOR_BOTON_GUARDAR, e -> guardar());
        JButton btnLimpiar = crearBoton("LIMPIAR FORMULARIO", new Color(108, 117, 125), e -> limpiar());

        pBtn.add(btnGuardar); pBtn.add(btnLimpiar);
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
            @Override public boolean isCellEditable(int row, int column) { return false; }
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
            cbAgente.removeAllItems();
            for (AgenteDTO a : agenteService.listarAgentes()) 
                cbAgente.addItem(new ItemCombo(a.getId(), a.getNombre()));

            cbCliente.removeAllItems();
            for (ClienteDTO c : clienteService.listarClientes()) 
                cbCliente.addItem(new ItemCombo(c.getIdCliente(), c.getNombreCompleto()));

            cbCampania.removeAllItems();
            for (CampaniaDTO c : campaniaService.listarCampanias()) 
                cbCampania.addItem(new ItemCombo(c.getIdCampania(), c.getNombreCampania()));
                
        } catch (Exception e) {
            mostrarError("Error al cargar listas desplegables: " + e.getMessage());
        }
    }

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

    private void guardar() {
        try {
            LlamadaDTO dto = new LlamadaDTO();
            dto.setFechaHora(LocalDateTime.now());
            
            // Validar numérico
            dto.setDuracion(Integer.parseInt(txtDuracion.getText()));
            dto.setDetalleResultado(txtDetalle.getText());
            dto.setObservaciones(txtObservacion.getText());

            // Obtener IDs de los objetos seleccionados en Combo
            ItemCombo ag = (ItemCombo) cbAgente.getSelectedItem();
            ItemCombo cl = (ItemCombo) cbCliente.getSelectedItem();
            ItemCombo cp = (ItemCombo) cbCampania.getSelectedItem();

            if (ag != null) dto.setIdAgente(ag.id);
            if (cl != null) dto.setIdCliente(cl.id);
            if (cp != null) dto.setIdCampania(cp.id);

            llamadaService.registrarLlamada(dto);
            mostrarExito("Llamada registrada correctamente");
            
            limpiar();
            cargarTabla();
            
        } catch (NumberFormatException ex) {
            mostrarError("La duración debe ser un número entero (segundos).");
        } catch (CallCenterException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtDuracion.setText("");
        txtDetalle.setText("");
        txtObservacion.setText("");
        if(cbAgente.getItemCount() > 0) cbAgente.setSelectedIndex(0);
        if(cbCliente.getItemCount() > 0) cbCliente.setSelectedIndex(0);
        if(cbCampania.getItemCount() > 0) cbCampania.setSelectedIndex(0);
    }

    // UTILIDADES
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(FUENTE_LABEL);
        return label;
    }

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

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clase auxiliar interna para manejar pares ID-Texto en los ComboBox.
     */
    private class ItemCombo {
        Long id;
        String texto;

        public ItemCombo(Long id, String texto) {
            this.id = id;
            this.texto = texto;
        }

        @Override
        public String toString() {
            return texto; // Esto es lo que muestra el ComboBox
        }
    }
}