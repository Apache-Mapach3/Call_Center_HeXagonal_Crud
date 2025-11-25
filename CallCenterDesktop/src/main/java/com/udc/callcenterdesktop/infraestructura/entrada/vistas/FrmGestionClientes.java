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
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
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

/**
 * Vista de Gestión de Clientes.
 * Sigue el patrón de "Inyección de Dependencias" al recibir la Interfaz del servicio.
 */
public class FrmGestionClientes extends JFrame {

    // Usar la Interfaz (IClienteService) en lugar de la clase concreta.
    // Esto desacopla la vista de la implementación lógica.
    private final IClienteService clienteService;
    
    // Componentes de la UI
    // Declaramos los campos como variables de clase para acceder a ellos desde los métodos guardar/limpiar
    private JTextField txtIdOculto;
    private JTextField txtNombre, txtDocumento, txtTelefono, txtEmail, txtDireccion;
    
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    /**
     * Constructor con Inyección de Dependencias.
     * @param service El servicio que contiene la lógica de negocio.
     */
    public FrmGestionClientes(IClienteService service) {
        this.clienteService = service;
        initUI();       // Construir la interfaz gráfica
        cargarTabla();  //Traer los datos iniciales de la BD
    }

    private void initUI() {
        setTitle("Gestión de Clientes - Call Center");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana, no toda la app
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());

        // HEADER  
        // Color diferente para distinguir visualmente 
        JPanel pHeader = new JPanel();
        pHeader.setBackground(new Color(34, 139, 34)); // Verde Bosque
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE CLIENTES");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        pHeader.add(lblTitulo);
        add(pHeader, BorderLayout.NORTH);

        // FORMULARIO
        // GridLayout 3, 4, 20, 30: 3 filas, 4 columnas, espaciado amplio para que se vea grande
        JPanel pForm = new JPanel(new GridLayout(3, 4, 20, 30));
        pForm.setBorder(BorderFactory.createTitledBorder("Datos Personales"));

        // Inicialización de campos (Es vital hacer esto antes de agregarlos)
        txtIdOculto = new JTextField(); txtIdOculto.setVisible(false);
        txtNombre = new JTextField();
        txtDocumento = new JTextField(); // Cédula
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();

        // Componentes usando el método auxiliar 'crearLabel' para código limpio
 
        pForm.add(crearLabel("Nombre Completo:")); pForm.add(txtNombre);
        pForm.add(crearLabel("Documento ID:"));    pForm.add(txtDocumento);
        pForm.add(crearLabel("Teléfono:"));        pForm.add(txtTelefono);
        pForm.add(crearLabel("Email:"));           pForm.add(txtEmail);
        pForm.add(crearLabel("Dirección:"));       pForm.add(txtDireccion);
        pForm.add(new JLabel(""));                 pForm.add(new JLabel("")); // Rellenos para cuadrar el grid

        // BOTONES
        JPanel pBtn = new JPanel(new FlowLayout());
        
        JButton btnGuardar = new JButton("GUARDAR / ACTUALIZAR");
        btnGuardar.setBackground(new Color(34, 139, 34)); // Verde para acción positiva
        btnGuardar.setForeground(Color.WHITE);
        
        JButton btnEliminar = new JButton("ELIMINAR");
        btnEliminar.setBackground(new Color(220, 53, 69)); // Rojo para acción destructiva
        btnEliminar.setForeground(Color.WHITE);
        
        JButton btnLimpiar = new JButton("LIMPIAR");

        // Uso de Lambdas  para asignar acciones de forma moderna
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        pBtn.add(btnGuardar); pBtn.add(btnEliminar); pBtn.add(btnLimpiar);

        // Contenedor Central que une formulario y botones
        JPanel pCentro = new JPanel(new BorderLayout());
        pCentro.add(pForm, BorderLayout.CENTER);
        pCentro.add(pBtn, BorderLayout.SOUTH);
        
        // El formulario al NORTE del BorderLayout principal
        // Así mantiene su tamaño y no es aplastado por la tabla.
        add(pCentro, BorderLayout.NORTH); // Antes estaba en CENTER

        // TABLA 
        String[] cols = {"ID", "Nombre", "Documento", "Teléfono", "Email", "Dirección"};
        modeloTabla = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; } // Tabla de solo lectura
        };
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(25); // Filas más altas para mejor lectura
        
        // Evento para cargar datos al hacer clic en una fila
        tablaClientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { seleccionar(); }
        });
        
        // Tabla en el CENTER para que ocupe todo el espacio sobrante
        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
    }

    // Método auxiliar para estandarizar etiquetas (DRY: Don't Repeat Yourself)
    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    /**
     * Llama al servicio para obtener la lista actualizada y refrescar la tabla.
     */
    private void cargarTabla() {
        if(clienteService == null) return;
        modeloTabla.setRowCount(0); // Limpiar tabla actual
        
        List<ClienteDTO> lista = clienteService.listarClientes();
        for(ClienteDTO c : lista) {
            modeloTabla.addRow(new Object[]{
                c.idCliente, 
                c.nombreCompleto, 
                c.documentoIdentidad, 
                c.telefono, 
                c.email, 
                c.direccion
            });
        }
    }

    /**
     * Lógica para Crear o Editar.
     * Usa el DTO para transportar los datos a la capa de servicio.
     */
    private void guardar() {
        try {
            // Empaquetar datos de la vista en un DTO
            ClienteDTO dto = new ClienteDTO();
            dto.nombreCompleto = txtNombre.getText();
            dto.documentoIdentidad = txtDocumento.getText();
            dto.telefono = txtTelefono.getText();
            dto.email = txtEmail.getText();
            dto.direccion = txtDireccion.getText();

            // Decidir si es Registro Nuevo o Actualización
            if (txtIdOculto.getText().isEmpty()) {
                clienteService.registrarCliente(dto);
            } else {
                dto.idCliente = Long.parseLong(txtIdOculto.getText());
                clienteService.actualizarCliente(dto);
            }
            
            // Feedback al usuario
            JOptionPane.showMessageDialog(this, "Operación exitosa.");
            limpiar(); 
            cargarTabla();
            
        } catch (CallCenterException ex) {
            // Capturamos Excepciones de Negocio (Validaciones lógicas)
            JOptionPane.showMessageDialog(this, "⚠️ " + ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            // Capturamos Excepciones Técnicas inesperadas
            JOptionPane.showMessageDialog(this, "Error crítico: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if(txtIdOculto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla primero.");
            return;
        }
        
        // Confirmación
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if(confirm != JOptionPane.YES_OPTION) return;

        try {
            clienteService.eliminarCliente(Long.parseLong(txtIdOculto.getText()));
            limpiar(); 
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Mueve los datos de la fila seleccionada a los campos de texto
    private void seleccionar() {
        int r = tablaClientes.getSelectedRow();
        if(r >= 0) {
            txtIdOculto.setText(modeloTabla.getValueAt(r, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(r, 1).toString());
            txtDocumento.setText(modeloTabla.getValueAt(r, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(r, 3).toString());
            txtEmail.setText(modeloTabla.getValueAt(r, 4).toString());
            txtDireccion.setText(modeloTabla.getValueAt(r, 5).toString());
        }
    }

    private void limpiar() {
        txtIdOculto.setText(""); 
        txtNombre.setText(""); 
        txtDocumento.setText("");
        txtTelefono.setText(""); 
        txtEmail.setText(""); 
        txtDireccion.setText("");
        tablaClientes.clearSelection();
    }
}