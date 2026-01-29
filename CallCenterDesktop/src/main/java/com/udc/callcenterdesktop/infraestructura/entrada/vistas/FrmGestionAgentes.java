package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmGestionAgentes extends JFrame {

    private final IAgenteService agenteService;
    
    // Componentes del formulario
    private JTextField txtNombre, txtCodigo, txtEmail, txtTelefono;
    private JComboBox<String> cmbTurno, cmbExperiencia;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnLimpiar, btnEliminar;

    public FrmGestionAgentes(IAgenteService service) {
        this.agenteService = service;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Agentes");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== CABECERA =====
        JLabel lblTitulo = new JLabel("Módulo de Agentes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // ===== FORMULARIO (IZQUIERDA) =====
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelFormulario.setBackground(new Color(240, 248, 255));

        panelFormulario.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Código Empleado:"));
        txtCodigo = new JTextField();
        panelFormulario.add(txtCodigo);

        panelFormulario.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Turno:"));
        cmbTurno = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
        panelFormulario.add(cmbTurno);

        panelFormulario.add(new JLabel("Experiencia:"));
        cmbExperiencia = new JComboBox<>(new String[]{"Junior", "Intermedio", "Senior"});
        panelFormulario.add(cmbExperiencia);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(0, 153, 76));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarAgente());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(204, 0, 0));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarAgente());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
        panelFormulario.add(panelBotones);

        add(panelFormulario, BorderLayout.WEST);

        // ===== TABLA (DERECHA) =====
        String[] columnas = {"ID", "Nombre", "Código", "Email", "Teléfono", "Turno", "Experiencia"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void guardarAgente() {
        try {
            AgenteDTO dto = new AgenteDTO();
            dto.setNombreCompleto(txtNombre.getText().trim());
            dto.setCodigoEmpleado(txtCodigo.getText().trim());
            dto.setEmail(txtEmail.getText().trim());
            dto.setTelefono(txtTelefono.getText().trim());
            dto.setTurno((String) cmbTurno.getSelectedItem());
            dto.setExperiencia((String) cmbExperiencia.getSelectedItem());

            agenteService.guardar(dto);
            JOptionPane.showMessageDialog(this, "Agente guardado correctamente");
            limpiarCampos();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        try {
            for (AgenteDTO agente : agenteService.listarTodos()) {
                modeloTabla.addRow(new Object[]{
                    agente.getIdAgente(),
                    agente.getNombreCompleto(),
                    agente.getCodigoEmpleado(),
                    agente.getEmail(),
                    agente.getTelefono(),
                    agente.getTurno(),
                    agente.getExperiencia()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCodigo.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        cmbTurno.setSelectedIndex(0);
        cmbExperiencia.setSelectedIndex(0);
    }

    private void eliminarAgente() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un agente de la tabla");
            return;
        }

        Long id = (Long) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar este agente?");
        
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                agenteService.eliminarAgente(id);
                JOptionPane.showMessageDialog(this, "Agente eliminado");
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}