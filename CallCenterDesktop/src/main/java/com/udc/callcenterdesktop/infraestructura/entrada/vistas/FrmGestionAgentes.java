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
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmGestionAgentes extends JFrame {

    private final IAgenteService agenteService;
    
    private JTextField txtId, txtNombre, txtNum, txtTel, txtEmail;
    private JComboBox<String> cbTurno, cbExp;
    private JTable tabla;
    private DefaultTableModel modelo;

    public FrmGestionAgentes(IAgenteService service) {
        this.agenteService = service;
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Gestión de Agentes");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Azul
        JPanel pH = new JPanel(); pH.setBackground(new Color(0, 102, 204));
        JLabel l = new JLabel("ADMINISTRACIÓN DE AGENTES"); l.setForeground(Color.WHITE); l.setFont(new Font("Arial", Font.BOLD, 20));
        pH.add(l); add(pH, BorderLayout.NORTH);

        // Formulario
        JPanel pF = new JPanel(new GridLayout(3, 4, 10, 10)); pF.setBorder(BorderFactory.createTitledBorder("Datos"));
        txtId = new JTextField(); txtId.setVisible(false);
        txtNombre = new JTextField(); txtNum = new JTextField(); txtTel = new JTextField(); txtEmail = new JTextField();
        cbTurno = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
        cbExp = new JComboBox<>(new String[]{"Junior", "Intermedio", "Senior"});
        
        pF.add(new JLabel("Nombre:")); pF.add(txtNombre);
        pF.add(new JLabel("Nro Emp:")); pF.add(txtNum);
        pF.add(new JLabel("Teléfono:")); pF.add(txtTel);
        pF.add(new JLabel("Email:")); pF.add(txtEmail);
        pF.add(new JLabel("Turno:")); pF.add(cbTurno);
        pF.add(new JLabel("Nivel:")); pF.add(cbExp);

        // Botones
        JPanel pB = new JPanel();
        JButton b1 = new JButton("GUARDAR"); b1.addActionListener(e -> guardar());
        JButton b2 = new JButton("ELIMINAR"); b2.addActionListener(e -> eliminar());
        JButton b3 = new JButton("LIMPIAR"); b3.addActionListener(e -> limpiar());
        pB.add(b1); pB.add(b2); pB.add(b3);

        JPanel pC = new JPanel(new BorderLayout()); pC.add(pF, BorderLayout.CENTER); pC.add(pB, BorderLayout.SOUTH);
        add(pC, BorderLayout.CENTER);

        // Tabla
        modelo = new DefaultTableModel(null, new String[]{"ID", "Nombre", "Nro", "Tel", "Email", "Turno", "Nivel"});
        tabla = new JTable(modelo);
        tabla.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { sel(); } });
        add(new JScrollPane(tabla), BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<AgenteDTO> l = agenteService.listarAgentes();
        for (AgenteDTO a : l) modelo.addRow(new Object[]{a.id, a.nombre, a.numeroEmpleado, a.telefono, a.email, a.turno, a.experiencia});
    }

    private void guardar() {
        try {
            AgenteDTO d = new AgenteDTO(
                txtId.getText().isEmpty() ? null : Long.parseLong(txtId.getText()),
                txtNombre.getText(), txtNum.getText(), txtTel.getText(), txtEmail.getText(),
                cbTurno.getSelectedItem().toString(), cbExp.getSelectedItem().toString()
            );
            if (d.id == null) agenteService.registrarAgente(d);
            else agenteService.actualizarAgente(d);
            JOptionPane.showMessageDialog(this, "Éxito"); limpiar(); cargarTabla();
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error: " + e.getMessage()); }
    }

    private void eliminar() {
        if(txtId.getText().isEmpty()) return;
        agenteService.eliminarAgente(Long.parseLong(txtId.getText()));
        limpiar(); cargarTabla();
    }

    private void sel() {
        int r = tabla.getSelectedRow();
        if (r >= 0) {
            txtId.setText(modelo.getValueAt(r, 0).toString());
            txtNombre.setText(modelo.getValueAt(r, 1).toString());
            txtNum.setText(modelo.getValueAt(r, 2).toString());
            txtTel.setText(modelo.getValueAt(r, 3).toString());
            txtEmail.setText(modelo.getValueAt(r, 4).toString());
            cbTurno.setSelectedItem(modelo.getValueAt(r, 5).toString());
            cbExp.setSelectedItem(modelo.getValueAt(r, 6).toString());
        }
    }
    private void limpiar() { txtId.setText(""); txtNombre.setText(""); txtNum.setText(""); txtTel.setText(""); txtEmail.setText(""); }
}