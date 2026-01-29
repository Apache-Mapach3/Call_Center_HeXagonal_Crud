package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FrmGestionCampanias extends JFrame {

    private final ICampaniaService campaniaService;
    
    private JTextField txtNombre, txtSupervisor, txtFechaInicio, txtFechaFin;
    private JComboBox<String> cmbTipo;
    private JTextArea txtDescripcion;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnLimpiar;

    public FrmGestionCampanias(ICampaniaService service) {
        this.campaniaService = service;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Campañas");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== CABECERA =====
        JLabel lblTitulo = new JLabel("Módulo de Campañas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(255, 140, 0));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // ===== FORMULARIO =====
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelFormulario.setBackground(new Color(255, 248, 240));

        panelFormulario.add(new JLabel("Nombre Campaña:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Tipo:"));
        cmbTipo = new JComboBox<>(new String[]{"Ventas", "Retención", "Soporte", "Marketing"});
        panelFormulario.add(cmbTipo);

        panelFormulario.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        txtFechaInicio = new JTextField();
        panelFormulario.add(txtFechaInicio);

        panelFormulario.add(new JLabel("Fecha Fin (YYYY-MM-DD):"));
        txtFechaFin = new JTextField();
        panelFormulario.add(txtFechaFin);

        panelFormulario.add(new JLabel("Supervisor:"));
        txtSupervisor = new JTextField();
        panelFormulario.add(txtSupervisor);

        panelFormulario.add(new JLabel("Descripción:"));
        txtDescripcion = new JTextArea(3, 20);
        panelFormulario.add(new JScrollPane(txtDescripcion));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(255, 140, 0));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarCampania());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones);

        add(panelFormulario, BorderLayout.WEST);

        // ===== TABLA =====
        String[] columnas = {"ID", "Nombre", "Tipo", "Inicio", "Fin", "Supervisor"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        
        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void guardarCampania() {
        try {
            CampaniaDTO dto = new CampaniaDTO();
            dto.setNombreCampania(txtNombre.getText().trim());
            dto.setTipoCampania((String) cmbTipo.getSelectedItem());
            
            // Convertir fechas
            dto.setFechaInicio(LocalDate.parse(txtFechaInicio.getText().trim()));
            dto.setFechaFin(LocalDate.parse(txtFechaFin.getText().trim()));
            
            dto.setSupervisoresCargo(txtSupervisor.getText().trim());
            dto.setDescripcionObjetivos(txtDescripcion.getText().trim());

            campaniaService.guardar(dto);
            JOptionPane.showMessageDialog(this, "Campaña guardada correctamente");
            limpiarCampos();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        try {
            for (CampaniaDTO campania : campaniaService.listarTodas()) {
                modeloTabla.addRow(new Object[]{
                    campania.getIdCampania(),
                    campania.getNombreCampania(),
                    campania.getTipoCampania(),
                    campania.getFechaInicio() != null ? campania.getFechaInicio().format(fmt) : "",
                    campania.getFechaFin() != null ? campania.getFechaFin().format(fmt) : "",
                    campania.getSupervisoresCargo()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtSupervisor.setText("");
        txtDescripcion.setText("");
        cmbTipo.setSelectedIndex(0);
    }
}