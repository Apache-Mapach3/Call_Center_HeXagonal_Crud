/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Interfaz gráfica para la gestión de Clientes del Call Center.
 * * <p>Proporciona funcionalidades CRUD completas:</p>
 * <ul>
 * <li>Registrar nuevos clientes</li>
 * <li>Listar todos los clientes</li>
 * <li>Actualizar datos de clientes existentes</li>
 * <li>Eliminar clientes del sistema</li>
 * </ul>
 * * <p><b>Arquitectura:</b> Esta clase pertenece a la capa de Infraestructura
 * (Adaptador de Entrada) y se comunica con la capa de Aplicación mediante
 * el puerto de entrada {@link IClienteService}.</p>
 * * @author Jose
 * @version 2.0
 * @since 2025
 */
public class FrmGestionClientes extends JFrame {

    // DEPENDENCIAS
    private final IClienteService clienteService;
    
    // COMPONENTES UI
    private JTextField txtIdOculto;
    private JTextField txtNombre;
    private JTextField txtDocumento;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;
    
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    
    // CONSTANTES UI 
    private static final Color COLOR_HEADER = new Color(34, 139, 34); // Verde para distinguir Clientes
    private static final Color COLOR_BOTON_GUARDAR = new Color(40, 167, 69);
    private static final Color COLOR_BOTON_ELIMINAR = new Color(220, 53, 69);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 12);

    /**
     * Constructor con inyección de dependencias.
     * * @param service servicio de aplicación para operaciones de clientes
     * @throws IllegalArgumentException si el servicio es null
     */
    public FrmGestionClientes(IClienteService service) {
        if (service == null) {
            throw new IllegalArgumentException(
                "El servicio de clientes no puede ser null"
            );
        }
        this.clienteService = service;
        initUI();
        cargarTabla();
    }

    /**
     * Inicializa todos los componentes de la interfaz gráfica.
     */
    private void initUI() {
        // Configuración de la ventana principal
        setTitle("Gestión de Clientes - Call Center");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Agregar componentes
        add(crearPanelHeader(), BorderLayout.NORTH);
        add(crearPanelCentro(), BorderLayout.CENTER);
        add(crearPanelTabla(), BorderLayout.SOUTH);
    }

    /**
     * Crea el panel de encabezado con el título.
     * * @return panel de encabezado configurado
     */
    private JPanel crearPanelHeader() {
        JPanel pHeader = new JPanel();
        pHeader.setBackground(COLOR_HEADER);
        pHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE CLIENTES");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FUENTE_TITULO);
        
        pHeader.add(lblTitulo);
        return pHeader;
    }

    /**
     * Crea el panel central con formulario y botones.
     * * @return panel central configurado
     */
    private JPanel crearPanelCentro() {
        JPanel pCentro = new JPanel(new BorderLayout(10, 10));
        pCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        pCentro.add(crearPanelFormulario(), BorderLayout.CENTER);
        pCentro.add(crearPanelBotones(), BorderLayout.SOUTH);
        
        return pCentro;
    }

    /**
     * Crea el panel de formulario con todos los campos de entrada.
     * * @return panel de formulario configurado
     */
    private JPanel crearPanelFormulario() {
        JPanel pForm = new JPanel(new GridLayout(3, 4, 20, 30)); // Ajustado a 3 filas
        pForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Datos del Cliente",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        // Inicializar componentes
        txtIdOculto = new JTextField();
        txtIdOculto.setVisible(false);
        
        txtNombre = new JTextField();
        txtDocumento = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();

        // Agregar componentes al formulario
        pForm.add(crearLabel("Nombre Completo:"));
        pForm.add(txtNombre);
        pForm.add(crearLabel("Documento ID:"));
        pForm.add(txtDocumento);
        
        pForm.add(crearLabel("Teléfono:"));
        pForm.add(txtTelefono);
        pForm.add(crearLabel("Email:"));
        pForm.add(txtEmail);
        
        pForm.add(crearLabel("Dirección:"));
        pForm.add(txtDireccion);
        pForm.add(new JLabel("")); // Relleno
        pForm.add(new JLabel("")); // Relleno
        
        return pForm;
    }

    /**
     * Crea el panel de botones de acción.
     * * @return panel de botones configurado
     */
    private JPanel crearPanelBotones() {
        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton btnGuardar = crearBoton(
            "GUARDAR / ACTUALIZAR", 
            COLOR_BOTON_GUARDAR, 
            e -> guardar()
        );
        
        JButton btnEliminar = crearBoton(
            "ELIMINAR", 
            COLOR_BOTON_ELIMINAR, 
            e -> eliminar()
        );
        
        JButton btnLimpiar = crearBoton(
            "LIMPIAR", 
            new Color(108, 117, 125), 
            e -> limpiar()
        );

        pBtn.add(btnGuardar);
        pBtn.add(btnEliminar);
        pBtn.add(btnLimpiar);
        
        return pBtn;
    }

    /**
     * Crea el panel con la tabla de clientes.
     * * @return panel de tabla configurado
     */
    private JPanel crearPanelTabla() {
        JPanel pTabla = new JPanel(new BorderLayout());
        pTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Lista de Clientes Registrados",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        String[] columnas = {"ID", "Nombre", "Documento", "Teléfono", "Email", "Dirección"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };
        
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setRowHeight(25);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        
        // Evento de selección
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    seleccionarCliente();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        
        pTabla.add(scrollPane, BorderLayout.CENTER);
        return pTabla;
    }

    /**
     * Carga la tabla con los datos actuales de la base de datos.
     */
    private void cargarTabla() {
        try {
            modeloTabla.setRowCount(0); // Limpiar tabla
            
            List<ClienteDTO> clientes = clienteService.listarClientes();
            
            for (ClienteDTO cliente : clientes) {
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
            mostrarError("Error al cargar la lista de clientes: " + ex.getMessage());
        }
    }

    /**
     * Guarda o actualiza un cliente según si tiene ID o no.
     */
    private void guardar() {
        try {
            // Crear DTO con los datos del formulario
            ClienteDTO dto = new ClienteDTO();
            // Uso de Setters por encapsulamiento
            dto.setNombreCompleto(txtNombre.getText().trim());
            dto.setDocumentoIdentidad(txtDocumento.getText().trim());
            dto.setTelefono(txtTelefono.getText().trim());
            dto.setEmail(txtEmail.getText().trim());
            dto.setDireccion(txtDireccion.getText().trim());

            // Determinar si es creación o actualización
            if (txtIdOculto.getText().isEmpty()) {
                clienteService.registrarCliente(dto);
                mostrarExito("Cliente registrado exitosamente");
            } else {
                dto.setIdCliente(Long.parseLong(txtIdOculto.getText()));
                clienteService.actualizarCliente(dto);
                mostrarExito("Cliente actualizado exitosamente");
            }
            
            limpiar();
            cargarTabla();
            
        } catch (CallCenterException ex) {
            mostrarAdvertencia(ex.getMessage());
        } catch (NumberFormatException ex) {
            mostrarError("Error: ID inválido");
        } catch (Exception ex) {
            mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Elimina el cliente seleccionado previa confirmación.
     */
    private void eliminar() {
        if (txtIdOculto.getText().isEmpty()) {
            mostrarAdvertencia("Por favor, seleccione un cliente de la tabla primero");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar este cliente?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Long id = Long.parseLong(txtIdOculto.getText());
                clienteService.eliminarCliente(id);
                
                mostrarExito("Cliente eliminado exitosamente");
                limpiar();
                cargarTabla();
                
            } catch (CallCenterException ex) {
                mostrarAdvertencia(ex.getMessage());
            } catch (Exception ex) {
                mostrarError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    /**
     * Carga los datos del cliente seleccionado en el formulario.
     */
    private void seleccionarCliente() {
        int filaSeleccionada = tablaClientes.getSelectedRow();
        
        if (filaSeleccionada >= 0) {
            txtIdOculto.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtDocumento.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtEmail.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            txtDireccion.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiar() {
        txtIdOculto.setText("");
        txtNombre.setText("");
        txtDocumento.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
        tablaClientes.clearSelection();
        txtNombre.requestFocus();
    }

    // MÉTODOS DE UTILIDAD 

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(FUENTE_LABEL);
        return label;
    }

    private JButton crearBoton(String texto, Color color, java.awt.event.ActionListener action) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(action);
        return boton;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}