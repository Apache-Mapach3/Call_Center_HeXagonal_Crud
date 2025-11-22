/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class FrmGestionAgentes extends JFrame {

    private AgenteService agenteService;

    // Componentes
    private JTextField txtNombre, txtNumeroEmpleado, txtTelefono, txtEmail;
    private JComboBox<String> cbHorario, cbExperiencia;
    private JButton btnGuardar, btnLimpiar;

    // Constructor vacio
    public FrmGestionAgentes() {
        initUI();
    }

    // Constructor con servicio
    public FrmGestionAgentes(AgenteService service) {
        this.agenteService = service;
        initUI();
    }

    private void initUI() {
        setTitle("Gestión de Agentes");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Titulo
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 102, 204));
        JLabel lblTitulo = new JLabel("Registro de Agentes");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 15));
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        txtNombre = new JTextField();
        txtNumeroEmpleado = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        cbHorario = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
        cbExperiencia = new JComboBox<>(new String[]{"Junior", "Intermedio", "Senior"});

        panelForm.add(crearLabel("Nombre Completo:")); panelForm.add(txtNombre);
        panelForm.add(crearLabel("ID Empleado:")); panelForm.add(txtNumeroEmpleado);
        panelForm.add(crearLabel("Teléfono:")); panelForm.add(txtTelefono);
        panelForm.add(crearLabel("Email:")); panelForm.add(txtEmail);
        panelForm.add(crearLabel("Horario:")); panelForm.add(cbHorario);
        panelForm.add(crearLabel("Experiencia:")); panelForm.add(cbExperiencia);

        // Botones
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnGuardar = new JButton("GUARDAR");
        btnLimpiar = new JButton("LIMPIAR");
        
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        
        // Acción Guardar
        btnGuardar.addActionListener((ActionEvent e) -> guardar());
        btnLimpiar.addActionListener((ActionEvent e) -> limpiar());

        panelBtn.add(btnGuardar);
        panelBtn.add(btnLimpiar);

        add(panelTitulo, BorderLayout.NORTH);
        add(panelForm, BorderLayout.CENTER);
        add(panelBtn, BorderLayout.SOUTH);
    }

    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }

    private void guardar() {
        if (agenteService == null) {
            JOptionPane.showMessageDialog(this, "Modo diseño (Sin conexión)");
            return;
        }
        // Crear objeto y mandar a guardar
        try {
            Agente a = new Agente(null, 
                txtNombre.getText(), 
                txtNumeroEmpleado.getText(), 
                txtTelefono.getText(), 
                txtEmail.getText(), 
                (String)cbHorario.getSelectedItem(), 
                (String)cbExperiencia.getSelectedItem()
            );
            agenteService.registrarAgente(a);
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtNumeroEmpleado.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }
}