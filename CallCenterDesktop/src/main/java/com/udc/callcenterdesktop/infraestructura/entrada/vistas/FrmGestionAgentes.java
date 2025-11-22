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

/**
 * Ventana de Gestión de Agentes.
 * Incluye Formulario (Arriba) y Tabla de Datos (Abajo).
 */
public class FrmGestionAgentes extends JFrame {

    private AgenteService agenteService;
    
    // Componentes del Formulario
    private JTextField txtIdOculto; // Para saber a quién editar
    private JTextField txtNombre, txtNumeroEmpleado, txtTelefono, txtEmail;
    private JComboBox<String> cbHorario, cbExperiencia;
    
    // Botones
    private JButton btnGuardar, btnEliminar, btnLimpiar;
    
    // Componentes de la Tabla
    private JTable tablaAgentes;
    private DefaultTableModel modeloTabla; // Es el "excel" detrás de la tabla

    public FrmGestionAgentes() {
        initUI();
    }

    public FrmGestionAgentes(AgenteService service) {
        this.agenteService = service;
        initUI();
        cargarTabla(); // Cargar datos al abrir la ventana
    }

    private void initUI() {
        setTitle("Gestión de Agentes - CRUD Completo");
        setSize(800, 600); // Más grande para que quepa la tabla
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // TITULO
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(0, 102, 204));
        JLabel lblTitulo = new JLabel("Administración de Agentes");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);

        // FORMULARIO (Panel Norte - Centro)
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(3, 4, 10, 10)); // 3 filas, 4 columnas
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Agente"));

        txtIdOculto = new JTextField(); 
        txtIdOculto.setVisible(false); // No se ve, pero guarda el ID

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

        // BOTONES (Debajo del formulario)
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGuardar = new JButton("GUARDAR / ACTUALIZAR");
        btnEliminar = new JButton("ELIMINAR");
        btnLimpiar = new JButton("LIMPIAR");
        
        btnGuardar.setBackground(new Color(40, 167, 69)); btnGuardar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(220, 53, 69)); btnEliminar.setForeground(Color.WHITE);
        
        // Eventos de Botones
        btnGuardar.addActionListener((ActionEvent e) -> guardar());
        btnEliminar.addActionListener((ActionEvent e) -> eliminar());
        btnLimpiar.addActionListener((ActionEvent e) -> limpiar());

        panelBtn.add(btnGuardar);
        panelBtn.add(btnEliminar);
        panelBtn.add(btnLimpiar);

        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBtn, BorderLayout.SOUTH);

        // TABLA (Panel Central - Abajo)
        // Definimos las columnas
        String[] columnas = {"ID", "Nombre", "Empleado #", "Teléfono", "Email", "Turno", "Nivel"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override // Hacemos que no se pueda editar directo en la celda
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tablaAgentes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaAgentes); // Barra de scroll
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Listado de Agentes (Clic para editar)"));

        // Evento al hacer clic en una fila (Para Editar)
        tablaAgentes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila();
            }
        });

        // AGREGAR TODO AL FRAME
        add(panelTitulo, BorderLayout.NORTH);
        add(panelSuperior, BorderLayout.CENTER); // Formulario al centro arriba
        add(scrollTabla, BorderLayout.SOUTH);    // Tabla abajo
        
        // Ajustar tamaño del panel sur para que la tabla se vea bien
        scrollTabla.setPreferredSize(new java.awt.Dimension(800, 250));
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    // LÓGICA DEL NEGOCIO

    private void cargarTabla() {
        if (agenteService == null) return;
        
        // 1. Limpiar tabla actual
        modeloTabla.setRowCount(0);
        
        // 2. Pedir lista al servicio
        List<Agente> lista = agenteService.listarAgentes();
        
        // 3. Llenar tabla
        for (Agente a : lista) {
            modeloTabla.addRow(new Object[]{
                a.getIdAgente(),
                a.getNombreCompleto(),
                a.getNumeroEmpleado(),
                a.getTelefonoContacto(),
                a.getEmail(),
                a.getHorarioTurno(),
                a.getNivelExperiencia()
            });
        }
    }

    private void guardar() {
        try {
            Agente a = new Agente();
            a.setNombreCompleto(txtNombre.getText());
            a.setNumeroEmpleado(txtNumeroEmpleado.getText());
            a.setTelefonoContacto(txtTelefono.getText());
            a.setEmail(txtEmail.getText());
            a.setHorarioTurno((String)cbHorario.getSelectedItem());
            a.setNivelExperiencia((String)cbExperiencia.getSelectedItem());

            // Verificamos si es NUEVO o EDICIÓN
            if (txtIdOculto.getText().isEmpty()) {
                // Es nuevo
                agenteService.registrarAgente(a);
            } else {
                // Es edición (ponemos el ID)
                a.setIdAgente(Long.parseLong(txtIdOculto.getText()));
                agenteService.actualizarAgente(a);
            }
            
            limpiar();
            cargarTabla(); // Refrescar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (txtIdOculto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un agente de la tabla primero.");
            return;
        }
        try {
            Long id = Long.parseLong(txtIdOculto.getText());
            agenteService.eliminarAgente(id);
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void seleccionarFila() {
        int fila = tablaAgentes.getSelectedRow();
        if (fila >= 0) {
            // Pasar datos de la tabla a las cajas de texto
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