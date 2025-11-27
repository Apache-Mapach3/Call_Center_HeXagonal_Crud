/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Interfaz gráfica para la gestión de Agentes del Call Center.
 * 
 * <p>Proporciona funcionalidades CRUD completas:</p>
 * <ul>
 *   <li>Registrar nuevos agentes</li>
 *   <li>Listar todos los agentes</li>
 *   <li>Actualizar datos de agentes existentes</li>
 *   <li>Eliminar agentes del sistema</li>
 * </ul>
 * 
 * <p><b>Arquitectura:</b> Esta clase pertenece a la capa de Infraestructura
 * (Adaptador de Entrada) y se comunica con la capa de Aplicación mediante
 * el puerto de entrada {@link IAgenteService}.</p>
 * 
 * @author Jose 
 * @version 2.0
 * @since 2025
 */
public class FrmGestionAgentes extends JFrame {

    // DEPENDENCIAS
    private final IAgenteService agenteService;
    
    //COMPONENTES UI
    private JTextField txtIdOculto;
    private JTextField txtNombre;
    private JTextField txtNumeroEmpleado;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JComboBox<String> cbHorario;
    private JComboBox<String> cbExperiencia;
    private JTable tablaAgentes;
    private DefaultTableModel modeloTabla;
    
    // CONSTANTES UI 
    private static final Color COLOR_HEADER = new Color(0, 102, 204);
    private static final Color COLOR_BOTON_GUARDAR = new Color(40, 167, 69);
    private static final Color COLOR_BOTON_ELIMINAR = new Color(220, 53, 69);
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 20);
    private static final Font FUENTE_LABEL = new Font("Arial", Font.BOLD, 12);

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param service servicio de aplicación para operaciones de agentes
     * @throws IllegalArgumentException si el servicio es null
     */
    public FrmGestionAgentes(IAgenteService service) {
        if (service == null) {
            throw new IllegalArgumentException(
                "El servicio de agentes no puede ser null"
            );
        }
        this.agenteService = service;
        initUI();
        cargarTabla();
    }

    /**
     * Inicializa todos los componentes de la interfaz gráfica.
     */
    private void initUI() {
        // Configuración de la ventana principal
        setTitle("Gestión de Agentes - Call Center");
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
     * 
     * @return panel de encabezado configurado
     */
    private JPanel crearPanelHeader() {
        JPanel pHeader = new JPanel();
        pHeader.setBackground(COLOR_HEADER);
        pHeader.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE AGENTES");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(FUENTE_TITULO);
        
        pHeader.add(lblTitulo);
        return pHeader;
    }

    /**
     * Crea el panel central con formulario y botones.
     * 
     * @return panel central configurado
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
     * 
     * @return panel de formulario configurado
     */
    private JPanel crearPanelFormulario() {
        JPanel pForm = new JPanel(new GridLayout(3, 3, 14, 14));
        pForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Datos del Agente",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        // Inicializar componentes
        txtIdOculto = new JTextField();
        txtIdOculto.setVisible(false);
        
        txtNombre = new JTextField();
        txtNumeroEmpleado = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        
        cbHorario = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche", "Mixto"});
        cbExperiencia = new JComboBox<>(new String[]{"Junior", "Intermedio", "Senior", "Expert"});

        // Agregar componentes al formulario
        pForm.add(crearLabel("Nombre Completo:"));
        pForm.add(txtNombre);
        pForm.add(crearLabel("Nro. Empleado:"));
        pForm.add(txtNumeroEmpleado);
        
        pForm.add(crearLabel("Teléfono:"));
        pForm.add(txtTelefono);
        pForm.add(crearLabel("Email:"));
        pForm.add(txtEmail);
        
        pForm.add(crearLabel("Turno:"));
        pForm.add(cbHorario);
        pForm.add(crearLabel("Experiencia:"));
        pForm.add(cbExperiencia);
        
        return pForm;
    }

    /**
     * Crea el panel de botones de acción.
     * 
     * @return panel de botones configurado
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
     * Crea el panel con la tabla de agentes.
     * 
     * @return panel de tabla configurado
     */
    private JPanel crearPanelTabla() {
        JPanel pTabla = new JPanel(new BorderLayout());
        pTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COLOR_HEADER, 2),
            "Lista de Agentes Registrados",
            0,
            0,
            FUENTE_LABEL,
            COLOR_HEADER
        ));

        String[] columnas = {"ID", "Nombre", "Nro Emp", "Teléfono", "Email", "Turno", "Experiencia"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla de solo lectura
            }
        };
        
        tablaAgentes = new JTable(modeloTabla);
        tablaAgentes.setRowHeight(25);
        tablaAgentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAgentes.getTableHeader().setReorderingAllowed(false);
        
        // Evento de selección
        tablaAgentes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    seleccionarAgente();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaAgentes);
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
            
            java.util.List<AgenteDTO> agentes = agenteService.listarAgentes();
            
            for (AgenteDTO agente : agentes) {
                modeloTabla.addRow(new Object[]{
                    agente.getId(),
                    agente.getNombre(),
                    agente.getNumeroEmpleado(),
                    agente.getTelefono(),
                    agente.getEmail(),
                    agente.getTurno(),
                    agente.getExperiencia()
                });
            }
            
        } catch (Exception ex) {
            mostrarError("Error al cargar la lista de agentes: " + ex.getMessage());
        }
    }

    /**
     * Guarda o actualiza un agente según si tiene ID o no.
     */
    private void guardar() {
        try {
            // Crear DTO con los datos del formulario
            AgenteDTO dto = new AgenteDTO();
            dto.setNombre(txtNombre.getText().trim());
            dto.setNumeroEmpleado(txtNumeroEmpleado.getText().trim());
            dto.setTelefono(txtTelefono.getText().trim());
            dto.setEmail(txtEmail.getText().trim());
            dto.setTurno((String) cbHorario.getSelectedItem());
            dto.setExperiencia((String) cbExperiencia.getSelectedItem());

            // Determinar si es creación o actualización
            if (txtIdOculto.getText().isEmpty()) {
                agenteService.registrarAgente(dto);
                mostrarExito("Agente registrado exitosamente");
            } else {
                dto.setId(Long.parseLong(txtIdOculto.getText()));
                agenteService.actualizarAgente(dto);
                mostrarExito("Agente actualizado exitosamente");
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
     * Elimina el agente seleccionado previa confirmación.
     */
    private void eliminar() {
        if (txtIdOculto.getText().isEmpty()) {
            mostrarAdvertencia("Por favor, seleccione un agente de la tabla primero");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que desea eliminar este agente?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Long id = Long.parseLong(txtIdOculto.getText());
                agenteService.eliminarAgente(id);
                
                mostrarExito("Agente eliminado exitosamente");
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
     * Carga los datos del agente seleccionado en el formulario.
     */
    private void seleccionarAgente() {
        int filaSeleccionada = tablaAgentes.getSelectedRow();
        
        if (filaSeleccionada >= 0) {
            txtIdOculto.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtNumeroEmpleado.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtTelefono.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtEmail.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            cbHorario.setSelectedItem(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            cbExperiencia.setSelectedItem(modeloTabla.getValueAt(filaSeleccionada, 6).toString());
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiar() {
        txtIdOculto.setText("");
        txtNombre.setText("");
        txtNumeroEmpleado.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        cbHorario.setSelectedIndex(0);
        cbExperiencia.setSelectedIndex(0);
        tablaAgentes.clearSelection();
        txtNombre.requestFocus();
    }

    // MÉTODOS DE UTILIDAD 

    /**
     * Crea un JLabel con formato estándar.
     * 
     * @param texto texto del label
     * @return JLabel configurado
     */
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(FUENTE_LABEL);
        return label;
    }

    /**
     * Crea un botón con configuración estándar.
     * 
     * @param texto texto del botón
     * @param color color de fondo
     * @param action acción al hacer clic
     * @return JButton configurado
     */
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

    /**
     * Muestra un mensaje de error.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Muestra un mensaje de advertencia.
     */
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Advertencia",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Muestra un mensaje de éxito.
     */
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}