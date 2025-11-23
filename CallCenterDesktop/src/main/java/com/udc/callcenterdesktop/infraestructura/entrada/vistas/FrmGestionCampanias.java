/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;


// IMPORTS DEL PROYECTO

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmGestionCampanias extends JFrame {

    private final ICampaniaService campaniaService;
    
    private JTextField txtId, txtNombre, txtSupervisor, txtInicio, txtFin, txtDescripcion;
    private JComboBox<String> cbTipo;
    private JTable tablaCampanias;
    private DefaultTableModel modeloTabla;

    public FrmGestionCampanias(ICampaniaService service) {
        this.campaniaService = service;
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Gestión de Campañas");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Naranja
        JPanel pHeader = new JPanel();
        pHeader.setBackground(new Color(255, 87, 34));
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE CAMPAÑAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        pHeader.add(lblTitulo);
        add(pHeader, BorderLayout.NORTH);

        // Formulario
        JPanel pForm = new JPanel(new GridLayout(3, 4, 15, 15));
        pForm.setBorder(BorderFactory.createTitledBorder("Datos de Campaña"));

        txtId = new JTextField(); txtId.setVisible(false);
        txtNombre = new JTextField(); txtSupervisor = new JTextField();
        txtInicio = new JTextField(); txtInicio.setToolTipText("YYYY-MM-DD");
        txtFin = new JTextField(); txtFin.setToolTipText("YYYY-MM-DD");
        txtDescripcion = new JTextField();
        cbTipo = new JComboBox<>(new String[]{"Ventas", "Soporte", "Encuesta", "Retención"});

        pForm.add(new JLabel("Nombre:")); pForm.add(txtNombre);
        pForm.add(new JLabel("Tipo:")); pForm.add(cbTipo);
        pForm.add(new JLabel("Supervisor:")); pForm.add(txtSupervisor);
        pForm.add(new JLabel("Inicio (YYYY-MM-DD):")); pForm.add(txtInicio);
        pForm.add(new JLabel("Fin (YYYY-MM-DD):")); pForm.add(txtFin);
        pForm.add(new JLabel("Descripción:")); pForm.add(txtDescripcion);

        // Botones
        JPanel pBtn = new JPanel();
        JButton btnGuardar = new JButton("GUARDAR");
        JButton btnEliminar = new JButton("ELIMINAR");
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
        modeloTabla = new DefaultTableModel(null, new String[]{"ID", "Nombre", "Tipo", "Inicio", "Fin", "Supervisor"});
        tablaCampanias = new JTable(modeloTabla);
        tablaCampanias.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { seleccionar(); }
        });
        add(new JScrollPane(tablaCampanias), BorderLayout.SOUTH);
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<CampaniaDTO> lista = campaniaService.listarCampanias();
        for(CampaniaDTO c : lista) {
            modeloTabla.addRow(new Object[]{c.idCampania, c.nombreCampania, c.tipoCampania, c.fechaInicio, c.fechaFin, c.supervisoresCargo});
        }
    }

    private void guardar() {
        try {
            CampaniaDTO dto = new CampaniaDTO();
            dto.nombreCampania = txtNombre.getText();
            dto.tipoCampania = (String) cbTipo.getSelectedItem();
            dto.fechaInicio = LocalDate.parse(txtInicio.getText());
            dto.fechaFin = LocalDate.parse(txtFin.getText());
            dto.supervisoresCargo = txtSupervisor.getText();
            dto.descripcionObjetivos = txtDescripcion.getText();

            if (txtId.getText().isEmpty()) campaniaService.registrarCampania(dto);
            else {
                dto.idCampania = Long.parseLong(txtId.getText());
                campaniaService.actualizarCampania(dto);
            }
            JOptionPane.showMessageDialog(this, "Éxito");
            limpiar(); cargarTabla();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Use formato YYYY-MM-DD");
        } catch (CallCenterException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if(txtId.getText().isEmpty()) return;
        campaniaService.eliminarCampania(Long.parseLong(txtId.getText()));
        limpiar(); cargarTabla();
    }

    private void seleccionar() {
        int r = tablaCampanias.getSelectedRow();
        if(r >= 0) {
            txtId.setText(modeloTabla.getValueAt(r, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(r, 1).toString());
            cbTipo.setSelectedItem(modeloTabla.getValueAt(r, 2).toString());
            txtInicio.setText(modeloTabla.getValueAt(r, 3).toString());
            txtFin.setText(modeloTabla.getValueAt(r, 4).toString());
            txtSupervisor.setText(modeloTabla.getValueAt(r, 5).toString());
        }
    }
    
    private void limpiar() {
        txtId.setText(""); txtNombre.setText(""); txtInicio.setText(""); txtFin.setText(""); txtSupervisor.setText(""); txtDescripcion.setText("");
    }
}
    
    
    
    
    
    
    
    
    
    

