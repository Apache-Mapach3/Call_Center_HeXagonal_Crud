package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmGestionClientes extends JFrame {

    private final IClienteService clienteService;
    
    private JTextField txtNombre, txtDocumento, txtTelefono, txtEmail, txtDireccion;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnLimpiar, btnEliminar;

    public FrmGestionClientes(IClienteService service) {
        this.clienteService = service;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Gestión de Clientes");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== CABECERA =====
        JLabel lblTitulo = new JLabel("Módulo de Clientes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(34, 139, 34));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // ===== FORMULARIO =====
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelFormulario.setBackground(new Color(240, 255, 240));

        panelFormulario.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Documento:"));
        txtDocumento = new JTextField();
        panelFormulario.add(txtDocumento);

        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelFormulario.add(txtEmail);

        panelFormulario.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelFormulario.add(txtDireccion);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(34, 139, 34));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardarCliente());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(204, 0, 0));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarCliente());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnEliminar);
        panelFormulario.add(panelBotones);

        add(panelFormulario, BorderLayout.WEST);

        // ===== TABLA =====
        String[] columnas = {"ID", "Nombre", "Documento", "Teléfono", "Email", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        
        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void guardarCliente() {
        try {
            ClienteDTO dto = new ClienteDTO();
            dto.setNombreCompleto(txtNombre.getText().trim());
            dto.setDocumentoIdentidad(txtDocumento.getText().trim());
            dto.setTelefono(txtTelefono.getText().trim());
            dto.setEmail(txtEmail.getText().trim());
            dto.setDireccion(txtDireccion.getText().trim());

            clienteService.guardar(dto);
            JOptionPane.showMessageDialog(this, "Cliente guardado correctamente");
            limpiarCampos();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        try {
            for (ClienteDTO cliente : clienteService.listarTodos()) {
                modeloTabla.addRow(new Object[]{
                    cliente.getIdCliente(),
                    cliente.getNombreCompleto(),
                    cliente.getDocumentoIdentidad(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getDireccion()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtDocumento.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
    }

    private void eliminarCliente() {
        int filaSeleccionada = tabla.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla");
            return;
        }

        Long id = (Long) modeloTabla.getValueAt(filaSeleccionada, 0);
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar este cliente?");
        
        if (confirmar == JOptionPane.YES_OPTION) {
            try {
                clienteService.eliminar(id);
                JOptionPane.showMessageDialog(this, "Cliente eliminado");
                cargarDatos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}