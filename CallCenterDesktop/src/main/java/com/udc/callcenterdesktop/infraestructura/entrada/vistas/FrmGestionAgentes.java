/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO; 
import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class FrmGestionAgentes extends JFrame {

    private AgenteService agenteService;
    
    // Componentes
    private JTextField txtIdOculto;
    private JTextField txtNombre, txtNumeroEmpleado, txtTelefono, txtEmail;
    private JComboBox<String> cbHorario, cbExperiencia;
    private JButton btnGuardar, btnEliminar, btnLimpiar;
    private JTable tablaAgentes;
    private DefaultTableModel modeloTabla;

    public FrmGestionAgentes() {
        initUI();
    }

    public FrmGestionAgentes(AgenteService service) {
        this.agenteService = service;
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Gestión de Agentes - Arquitectura Hexagonal + DTOs");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TITULO
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 102, 204));
        JLabel lblTitulo = new JLabel("Administración de Agentes)");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);

        // FORMULARIO
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(3, 4, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Agente"));

        txtIdOculto = new JTextField(); txtIdOculto.setVisible(false);
        txtNombre = new JTextField();
        txtNumeroEmpleado = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        cbHorario = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
        cbExperiencia = new JComboBox<>(new String[]{"Junior", "Intermedio", "Senior"});

        panelForm.add(crearLabel("Nombre:")); panelForm.add(txtNombre);
        panelForm.add(crearLabel("Nro Empleado:")); panelForm.add(txtNumeroEmpleado);
        panelForm.add(crearLabel("Teléfono:")); panelForm.add(txtTelefono);
        panelForm.add(crearLabel("Email:")); panelForm.add(txtEmail);
        panelForm.add(crearLabel("Turno:")); panelForm.add(cbHorario);
        panelForm.add(crearLabel("Experiencia:")); panelForm.add(cbExperiencia);

        // BOTONES
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("GUARDAR / ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");
        btnLimpiar = new JButton("LIMPIAR");
        
        btnGuardar.setBackground(new Color(40, 167, 69)); btnGuardar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 53, 69)); btnEliminar.setForeground(Color.WHITE);
        
        btnGuardar.addActionListener((ActionEvent e) -> guardar());
        btnEliminar.addActionListener((ActionEvent e) -> eliminar());
        btnLimpiar.addActionListener((ActionEvent e) -> limpiar());

        panelBtn.add(btnGuardar);
        panelBtn.add(btnEliminar);
        panelBtn.add(btnLimpiar);

        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBtn, BorderLayout.SOUTH);

        // TABLA
        String[] columnas = {"ID", "Nombre", "Empleado #", "Teléfono", "Email", "Turno", "Nivel"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaAgentes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaAgentes);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de Agentes"));

        tablaAgentes.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { seleccionarFila(); }
        });

        add(panelTitulo, BorderLayout.NORTH);
        add(panelSuperior, BorderLayout.CENTER);
        add(scrollTabla, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    // LÓGICA ACTUALIZADA CON DTOs 

    private void cargarTabla() {
        if (agenteService == null) return;
        modeloTabla.setRowCount(0);
        
        // AHORA RECIBIMOS DTOs, NO ENTIDADES
        List<AgenteDTO> lista = agenteService.listarAgentes();
        
        for (AgenteDTO dto : lista) {
            modeloTabla.addRow(new Object[]{
                dto.id,       // Acceso directo a los campos públicos del DTO
                dto.nombre,
                dto.numeroEmpleado,
                dto.telefono,
                dto.email,
                dto.turno,
                dto.experiencia
            });
        }
    }

    private void guardar() {
        try {
            // CREAMOS UN DTO, NO UNA ENTIDAD
            AgenteDTO dto = new AgenteDTO();
            dto.nombre = txtNombre.getText();
            dto.numeroEmpleado = txtNumeroEmpleado.getText();
            dto.telefono = txtTelefono.getText();
            dto.email = txtEmail.getText();
            dto.turno = (String)cbHorario.getSelectedItem();
            dto.experiencia = (String)cbExperiencia.getSelectedItem();

            if (txtIdOculto.getText().isEmpty()) {
                agenteService.registrarAgente(dto);
            } else {
                dto.id = Long.parseLong(txtIdOculto.getText());
                agenteService.actualizarAgente(dto);
            }
            
            JOptionPane.showMessageDialog(this, " Operación exitosa.");
            limpiar();
            cargarTabla();
            
        } catch (CallCenterException ex) {
            // Capturamos nuestra excepción personalizada
            JOptionPane.showMessageDialog(this, " x " + ex.getMessage(), "Error de Negocio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (txtIdOculto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un agente primero.");
            return;
        }
        try {
            Long id = Long.parseLong(txtIdOculto.getText());
            agenteService.eliminarAgente(id);
            limpiar();
            cargarTabla();
        } catch (CallCenterException ex) {
            JOptionPane.showMessageDialog(this, " x " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void seleccionarFila() {
        int fila = tablaAgentes.getSelectedRow();
        if (fila >= 0) {
            txtIdOculto.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNumeroEmpleado.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtEmail.setText(modeloTabla.getValueAt(fila, 4).toString());
            cbHorario.setSelectedItem(modeloTabla.getValueAt(fila, 5).toString());
            cbExperiencia.setSelectedItem(modeloTabla.getValueAt(fila, 6).toString());
        }
    }

    private void limpiar() {
        txtIdOculto.setText("");
        txtNombre.setText("");
        txtNumeroEmpleado.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        cbHorario.setSelectedIndex(0);
        tablaAgentes.clearSelection();
    }
}