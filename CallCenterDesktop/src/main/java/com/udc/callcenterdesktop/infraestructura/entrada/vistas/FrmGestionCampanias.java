/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;


// IMPORTS DEL PROYECTO

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Interfaz gráfica para la gestión de Campañas de Marketing.
 * * <p>Proporciona funcionalidades CRUD completas:</p>
 * <ul>
 * <li>Definir nuevas campañas</li>
 * <li>Listar campañas activas e históricas</li>
 * <li>Actualizar fechas y objetivos</li>
 * <li>Eliminar campañas</li>
 * </ul>
 * * <p><b>Arquitectura:</b> Adaptador de Entrada que se comunica con {@link ICampaniaService}.</p>
 * * @author Carlos
 * @version 2.0
 * @since 2025
 */
public class FrmGestionCampanias extends JFrame {

    // DEPENDENCIAS
    private final ICampaniaService campaniaService;
    
    // COMPONENTES UI
    private JTextField txtIdOculto;
    private JTextField txtNombre;
    private JComboBox<String> cbTipo;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtSupervisor;
    private JTextField txtObjetivos;
    
    private JTable tablaCampanias;
    private DefaultTableModel modeloTabla;
    
    // CONSTANTES UI 
    private static final Color COLOR_HEADER = new Color(255, 140, 0); // Naranja para Campañas
    private static final Color COLOR_BOTON_GUARDAR = new Color(40, 167, 69);
    private static final Color COLOR_BOTON_ELIMINAR = new Color(220, 53, 69);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 12);

    /**
     * Constructor con inyección de dependencias.
     * * @param service servicio de aplicación para campañas
     * @throws IllegalArgumentException si el servicio es null
     */
    public FrmGestionCampanias(ICampaniaService service) {
        if (service == null) {
            throw new IllegalArgumentException(
                "El servicio de campañas no puede ser null"
            );
        }
        this.campaniaService = service;
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Gestión de Campañas - Call Center");
        setSize(950, 700);
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
        
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE CAMPAÑAS");
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
        JPanel pForm = new JPanel(new GridLayout(3, 3, 14, 14));
        pForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Datos de la Campaña",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        // Inicializar componentes
        txtIdOculto = new JTextField(); txtIdOculto.setVisible(false);
        txtNombre = new JTextField();
        cbTipo = new JComboBox<>(new String[]{"Ventas", "Soporte", "Encuesta", "Retención", "Cobranza"});
        txtFechaInicio = new JTextField(); txtFechaInicio.setToolTipText("YYYY-MM-DD");
        txtFechaFin = new JTextField(); txtFechaFin.setToolTipText("YYYY-MM-DD");
        txtSupervisor = new JTextField();
        txtObjetivos = new JTextField();

        
        pForm.add(crearLabel("Nombre Campaña:")); pForm.add(txtNombre);
        pForm.add(crearLabel("Tipo:")); pForm.add(cbTipo);
        
       
        pForm.add(crearLabel("Fecha Inicio (YYYY-MM-DD):")); pForm.add(txtFechaInicio);
        pForm.add(crearLabel("Fecha Fin (YYYY-MM-DD):")); pForm.add(txtFechaFin);
        
        
        pForm.add(crearLabel("Supervisor:")); pForm.add(txtSupervisor);
        pForm.add(crearLabel("Objetivos:")); pForm.add(txtObjetivos);

        return pForm;
    }

    private JPanel crearPanelBotones() {
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnGuardar = crearBoton("GUARDAR / ACTUALIZAR", COLOR_BOTON_GUARDAR, e -> guardar());
        JButton btnEliminar = crearBoton("ELIMINAR", COLOR_BOTON_ELIMINAR, e -> eliminar());
        JButton btnLimpiar = crearBoton("LIMPIAR", new Color(108, 117, 125), e -> limpiar());

        pBtn.add(btnGuardar); pBtn.add(btnEliminar); pBtn.add(btnLimpiar);
        return pBtn;
    }

    private JPanel crearPanelTabla() {
        JPanel pTabla = new JPanel(new BorderLayout());
        pTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Historial de Campañas",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        String[] columnas = {"ID", "Nombre", "Tipo", "Inicio", "Fin", "Supervisor", "Objetivos"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaCampanias = new JTable(modeloTabla);
        tablaCampanias.setRowHeight(25);
        tablaCampanias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCampanias.getTableHeader().setReorderingAllowed(false);
        
        tablaCampanias.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) seleccionarCampania();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaCampanias);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        pTabla.add(scrollPane, BorderLayout.CENTER);
        return pTabla;
    }

    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0);
            List<CampaniaDTO> campanias = campaniaService.listarCampanias();
            
            for (CampaniaDTO c : campanias) {
                modeloTabla.addRow(new Object[]{
                    c.getIdCampania(),
                    c.getNombreCampania(),
                    c.getTipoCampania(),
                    c.getFechaInicio(),
                    c.getFechaFin(),
                    c.getSupervisoresCargo(),
                    c.getDescripcionObjetivos()
                });
            }
        } catch (Exception ex) {
            mostrarError("Error al cargar campañas: " + ex.getMessage());
        }
    }

    private void guardar() {
        try {
            CampaniaDTO dto = new CampaniaDTO();
            // Uso de setters privados
            dto.setNombreCampania(txtNombre.getText().trim());
            dto.setTipoCampania((String) cbTipo.getSelectedItem());
            dto.setSupervisoresCargo(txtSupervisor.getText().trim());
            dto.setDescripcionObjetivos(txtObjetivos.getText().trim());
            
            // Parseo de fechas (Manejo de formato YYYY-MM-DD)
            try {
                dto.setFechaInicio(LocalDate.parse(txtFechaInicio.getText().trim()));
                if(!txtFechaFin.getText().trim().isEmpty()){
                    dto.setFechaFin(LocalDate.parse(txtFechaFin.getText().trim()));
                }
            } catch (DateTimeParseException dtpe) {
                throw new CallCenterException("Formato de fecha inválido. Use YYYY-MM-DD");
            }

            if (txtIdOculto.getText().isEmpty()) {
                campaniaService.registrarCampania(dto);
                mostrarExito("Campaña creada exitosamente");
            } else {
                dto.setIdCampania(Long.parseLong(txtIdOculto.getText()));
                campaniaService.actualizarCampania(dto);
                mostrarExito("Campaña actualizada exitosamente");
            }
            
            limpiar();
            cargarTabla();
            
        } catch (CallCenterException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (Exception ex) {
            mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (txtIdOculto.getText().isEmpty()) {
            mostrarAdvertencia("Seleccione una campaña primero");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Eliminar esta campaña permanentemente?", "Confirmar", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Long id = Long.parseLong(txtIdOculto.getText());
                campaniaService.eliminarCampania(id);
                mostrarExito("Campaña eliminada");
                limpiar();
                cargarTabla();
            } catch (Exception ex) {
                mostrarError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private void seleccionarCampania() {
        int r = tablaCampanias.getSelectedRow();
        if (r >= 0) {
            txtIdOculto.setText(modeloTabla.getValueAt(r, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(r, 1).toString());
            cbTipo.setSelectedItem(modeloTabla.getValueAt(r, 2).toString());
            txtFechaInicio.setText(modeloTabla.getValueAt(r, 3).toString());
            
            Object fin = modeloTabla.getValueAt(r, 4);
            txtFechaFin.setText(fin != null ? fin.toString() : "");
            
            txtSupervisor.setText(modeloTabla.getValueAt(r, 5).toString());
            txtObjetivos.setText(modeloTabla.getValueAt(r, 6).toString());
        }
    }

    private void limpiar() {
        txtIdOculto.setText(""); txtNombre.setText(""); 
        cbTipo.setSelectedIndex(0);
        txtFechaInicio.setText(""); txtFechaFin.setText("");
        txtSupervisor.setText(""); txtObjetivos.setText("");
        tablaCampanias.clearSelection();
    }

    // UTILIDADES (Copias exactas del estilo original)
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
}

