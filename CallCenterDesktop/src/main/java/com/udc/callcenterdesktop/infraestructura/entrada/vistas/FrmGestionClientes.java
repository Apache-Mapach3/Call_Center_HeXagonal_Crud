/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana de gestión para el módulo de Clientes.
 * Permite crear, editar, eliminar y listar registros.
 */
public class FrmGestionClientes extends JFrame {

    private final IClienteService clienteService;
    
    // Componentes de la interfaz
    private JTextField txtId, txtNombre, txtDocumento, txtTelefono, txtEmail, txtDireccion;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    // Fuentes para estilizado
    private final Font fuenteTitulo = new Font("Arial", Font.BOLD, 22);
    private final Font fuenteCampos = new Font("Arial", Font.PLAIN, 14);

    public FrmGestionClientes(IClienteService service) {
        this.clienteService = service;
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Gestión de Clientes - Call Center");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ENCABEZADO 
        JPanel pHeader = new JPanel();
        pHeader.setBackground(new Color(34, 139, 34)); // Verde Forest
        pHeader.setPreferredSize(new Dimension(900, 60));
        
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE CLIENTES");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(fuenteTitulo);
        pHeader.add(lblTitulo);
        add(pHeader, BorderLayout.NORTH);

        // FORMULARIO 
        JPanel pForm = new JPanel(new GridLayout(3, 4, 15, 15));
        pForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos Personales"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Inicialización de campos
        txtId = new JTextField(); txtId.setVisible(false);
        txtNombre = crearInput();
        txtDocumento = crearInput();
        txtTelefono = crearInput();
        txtEmail = crearInput();
        txtDireccion = crearInput();

        // Agregar componentes al panel
        pForm.add(crearLabel("Nombre Completo:")); pForm.add(txtNombre);
        pForm.add(crearLabel("Documento ID:")); pForm.add(txtDocumento);
        pForm.add(crearLabel("Teléfono:")); pForm.add(txtTelefono);
        pForm.add(crearLabel("Email:")); pForm.add(txtEmail);
        pForm.add(crearLabel("Dirección:")); pForm.add(txtDireccion);

        // BOTONES
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnGuardar = crearBoton("GUARDAR / ACTUALIZAR", new Color(34, 139, 34));
        JButton btnEliminar = crearBoton("ELIMINAR", new Color(220, 53, 69));
        JButton btnLimpiar = crearBoton("LIMPIAR FORMULARIO", new Color(108, 117, 125));
        
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        
        pBtn.add(btnGuardar);
        pBtn.add(btnEliminar);
        pBtn.add(btnLimpiar);
        
        JPanel pCentro = new JPanel(new BorderLayout());
        pCentro.add(pForm, BorderLayout.CENTER);
        pCentro.add(pBtn, BorderLayout.SOUTH);
        add(pCentro, BorderLayout.CENTER);

        // TABLA
        modeloTabla = new DefaultTableModel(null, new String[]{"ID", "Nombre", "Doc", "Tel", "Email", "Dir"}){
            @Override
            public boolean isCellEditable(int r, int c){ return false; }
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(25);
        tablaClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.setBorder(BorderFactory.createTitledBorder("Listado de Clientes"));
        
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { seleccionarFila(); }
        });
        add(scroll, BorderLayout.SOUTH);
    }

    // Lógica de Negocio

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<ClienteDTO> lista = clienteService.listarClientes();
        for(ClienteDTO c : lista) {
            modeloTabla.addRow(new Object[]{
                c.idCliente, c.nombreCompleto, c.documentoIdentidad, 
                c.telefono, c.email, c.direccion
            });
        }
    }

    private void guardar() {
        try {
            ClienteDTO dto = new ClienteDTO();
            dto.nombreCompleto = txtNombre.getText();
            dto.documentoIdentidad = txtDocumento.getText();
            dto.telefono = txtTelefono.getText();
            dto.email = txtEmail.getText();
            dto.direccion = txtDireccion.getText();

            if (txtId.getText().isEmpty()) {
                clienteService.registrarCliente(dto);
            } else {
                dto.idCliente = Long.parseLong(txtId.getText());
                clienteService.actualizarCliente(dto);
            }
            
            JOptionPane.showMessageDialog(this, " Operación realizada con éxito.");
            limpiar(); 
            cargarTabla();
            
        } catch (CallCenterException ex) {
            JOptionPane.showMessageDialog(this, "!!" + ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error crítico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if(txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente primero.");
            return;
        }
        try {
            Long id = Long.parseLong(txtId.getText());
            clienteService.eliminarCliente(id);
            limpiar(); 
            cargarTabla();
            JOptionPane.showMessageDialog(this, " Cliente eliminado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void seleccionarFila() {
        int fila = tablaClientes.getSelectedRow();
        if(fila >= 0) {
            txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtDocumento.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtEmail.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtDireccion.setText(modeloTabla.getValueAt(fila, 5).toString());
        }
    }
    
    private void limpiar() {
        txtId.setText(""); 
        txtNombre.setText(""); 
        txtDocumento.setText(""); 
        txtTelefono.setText(""); 
        txtEmail.setText(""); 
        txtDireccion.setText("");
        tablaClientes.clearSelection();
    }

    // Métodos Auxiliares de Diseño
    private JTextField crearInput() {
        JTextField t = new JTextField();
        t.setFont(fuenteCampos);
        return t;
    }
    
    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        return l;
    }
    
    private JButton crearBoton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        return b;
    }
}