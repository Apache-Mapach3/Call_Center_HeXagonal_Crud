/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmGestionAgentes extends JFrame {

    // CAMBIO CLAVE: Usamos la Interfaz (I...)
    private final IAgenteService agenteService;
    
    // Componentes de la UI
    private JTextField txtIdOculto, txtNombre, txtNumeroEmpleado, txtTelefono, txtEmail;
    private JComboBox<String> cbHorario, cbExperiencia;
    private JTable tablaAgentes;
    private DefaultTableModel modeloTabla;

    // El constructor recibe la Interfaz
    public FrmGestionAgentes(IAgenteService service) {
        this.agenteService = service;
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Gestión de Agentes");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel pHeader = new JPanel();
        pHeader.setBackground(new Color(0, 102, 204));
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE AGENTES");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        pHeader.add(lblTitulo);
        add(pHeader, BorderLayout.NORTH);

        // Formulario
        JPanel pForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pForm.setBorder(BorderFactory.createTitledBorder("Datos del Agente"));

        txtIdOculto = new JTextField(); txtIdOculto.setVisible(false);
        txtNombre = new JTextField();
        txtNumeroEmpleado = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        cbHorario = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
        cbExperiencia = new JComboBox<>(new String[]{"Junior", "Intermedio", "Senior"});

        pForm.add(crearLabel("Nombre:")); pForm.add(txtNombre);
        pForm.add(crearLabel("Nro Empleado:")); pForm.add(txtNumeroEmpleado);
        pForm.add(crearLabel("Teléfono:")); pForm.add(txtTelefono);
        pForm.add(crearLabel("Email:")); pForm.add(txtEmail);
        pForm.add(crearLabel("Turno:")); pForm.add(cbHorario);
        pForm.add(crearLabel("Experiencia:")); pForm.add(cbExperiencia);

        // Botones
        JPanel pBtn = new JPanel(new FlowLayout());
        JButton btnGuardar = new JButton("GUARDAR / ACTUALIZAR");
        btnGuardar.setBackground(new Color(40, 167, 69)); btnGuardar.setForeground(Color.WHITE);
        
        JButton btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setBackground(new Color(220, 53, 69)); btnEliminar.setForeground(Color.WHITE);
        
        JButton btnLimpiar = new JButton("LIMPIAR");

        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        pBtn.add(btnGuardar); pBtn.add(btnEliminar); pBtn.add(btnLimpiar);

        JPanel pCentro = new JPanel(new BorderLayout());
        pCentro.add(pForm, BorderLayout.CENTER);
        pCentro.add(pBtn, BorderLayout.SOUTH);
        add(pCentro, BorderLayout.CENTER);

        // Tabla
        String[] cols = {"ID", "Nombre", "Nro Emp", "Tel", "Email", "Turno", "Nivel"};
        modeloTabla = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaAgentes = new JTable(modeloTabla);
        tablaAgentes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { seleccionar(); }
        });
        add(new JScrollPane(tablaAgentes), BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    private void cargarTabla() {
        if(agenteService == null) return;
        modeloTabla.setRowCount(0);
        List<AgenteDTO> lista = agenteService.listarAgentes();
        for(AgenteDTO a : lista) {
            modeloTabla.addRow(new Object[]{a.id, a.nombre, a.numeroEmpleado, a.telefono, a.email, a.turno, a.experiencia});
        }
    }

    private void guardar() {
        try {
            AgenteDTO dto = new AgenteDTO();
            dto.nombre = txtNombre.getText();
            dto.numeroEmpleado = txtNumeroEmpleado.getText();
            dto.telefono = txtTelefono.getText();
            dto.email = txtEmail.getText();
            dto.turno = (String) cbHorario.getSelectedItem();
            dto.experiencia = (String) cbExperiencia.getSelectedItem();

            if (txtIdOculto.getText().isEmpty()) {
                agenteService.registrarAgente(dto);
            } else {
                dto.id = Long.parseLong(txtIdOculto.getText());
                agenteService.actualizarAgente(dto);
            }
            JOptionPane.showMessageDialog(this, "Operación exitosa.");
            limpiar(); cargarTabla();
        } catch (CallCenterException ex) {
            JOptionPane.showMessageDialog(this, "!!️ " + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if(txtIdOculto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un agente.");
            return;
        }
        try {
            agenteService.eliminarAgente(Long.parseLong(txtIdOculto.getText()));
            limpiar(); cargarTabla();
            JOptionPane.showMessageDialog(this, "Agente eliminado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void seleccionar() {
        int r = tablaAgentes.getSelectedRow();
        if(r >= 0) {
            txtIdOculto.setText(modeloTabla.getValueAt(r, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(r, 1).toString());
            txtNumeroEmpleado.setText(modeloTabla.getValueAt(r, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(r, 3).toString());
            txtEmail.setText(modeloTabla.getValueAt(r, 4).toString());
            cbHorario.setSelectedItem(modeloTabla.getValueAt(r, 5).toString());
            cbExperiencia.setSelectedItem(modeloTabla.getValueAt(r, 6).toString());
        }
    }

    private void limpiar() {
        txtIdOculto.setText(""); txtNombre.setText(""); txtNumeroEmpleado.setText(""); 
        txtTelefono.setText(""); txtEmail.setText(""); cbHorario.setSelectedIndex(0);
        tablaAgentes.clearSelection();
    }
}