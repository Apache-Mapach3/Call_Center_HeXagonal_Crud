/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

    
    import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.aplicacion.servicios.CampaniaService;
import com.udc.callcenterdesktop.infraestructura.salida.persistencia.CampaniaMySqlAdapter;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class FrmGestionCampanias extends javax.swing.JFrame {

    // Dependencia de Servicio (Puerto de Entrada)
    private final ICampaniaService campaniaService;
    
    // Componentes de la GUI
    private DefaultTableModel tablaModelo;
    private JTable tablaCampanias;
    private JTextField txtId, txtNombre, txtTipo, txtFechaInicio, txtFechaFin, txtSupervisores, txtObjetivos;
    private JButton btnGuardar, btnEliminar, btnLimpiar, btnBuscar;

    public FrmGestionCampanias() {
        // Inicialización de la Arquitectura (Inyección de Dependencias)
        CampaniaMySqlAdapter repositorio = new CampaniaMySqlAdapter();
        this.campaniaService = new CampaniaService(repositorio);
        
        initComponentsPersonalizados();
        cargarDatosTabla();
        
        // Configuración inicial (Ej: ocultar ID para CRUD)
        if (tablaCampanias.getColumnModel().getColumnCount() > 0) {
            tablaCampanias.getColumnModel().getColumn(0).setMinWidth(0);
            tablaCampanias.getColumnModel().getColumn(0).setMaxWidth(0);
            tablaCampanias.getColumnModel().getColumn(0).setWidth(0);
        }
    }

    private void initComponentsPersonalizados() {
        setTitle("Gestión de Campañas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Panel de Datos (Norte) ---
        JPanel pnlDatos = new JPanel(new GridLayout(4, 4, 10, 10)); // 4 filas, 4 columnas
        pnlDatos.setBorder(BorderFactory.createTitledBorder("Detalles de la Campaña"));

        txtId = new JTextField(5);
        txtId.setEditable(false); // ID no editable, solo lectura
        txtNombre = new JTextField(20);
        txtTipo = new JTextField(20);
        txtFechaInicio = new JTextField("AAAA-MM-DD");
        txtFechaFin = new JTextField("AAAA-MM-DD");
        txtSupervisores = new JTextField(20);
        txtObjetivos = new JTextField(30);

        pnlDatos.add(new JLabel("ID:"));
        pnlDatos.add(txtId);
        pnlDatos.add(new JLabel("Nombre:"));
        pnlDatos.add(txtNombre);
        pnlDatos.add(new JLabel("Tipo:"));
        pnlDatos.add(txtTipo);
        pnlDatos.add(new JLabel("Fecha Inicio (AAAA-MM-DD):"));
        pnlDatos.add(txtFechaInicio);
        pnlDatos.add(new JLabel("Fecha Fin (AAAA-MM-DD):"));
        pnlDatos.add(txtFechaFin);
        pnlDatos.add(new JLabel("Supervisores:"));
        pnlDatos.add(txtSupervisores);
        pnlDatos.add(new JLabel("Objetivos:"));
        pnlDatos.add(txtObjetivos);
        
        // Rellenar espacios vacíos en el GridLayout
        pnlDatos.add(new JLabel("")); 
        pnlDatos.add(new JLabel("")); 

        // --- Panel de Botones (Sur) ---
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnGuardar = new JButton("Guardar/Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar Campos");
        btnBuscar = new JButton("Buscar");

        pnlBotones.add(btnGuardar);
        pnlBotones.add(btnEliminar);
        pnlBotones.add(btnLimpiar);
        pnlBotones.add(btnBuscar);

        // --- Panel de Tabla (Centro) ---
        String[] columnas = {"ID", "Nombre", "Tipo", "Fecha Inicio", "Fecha Fin", "Supervisores", "Objetivos"};
        tablaModelo = new DefaultTableModel(columnas, 0);
        tablaCampanias = new JTable(tablaModelo);
        JScrollPane scrollPanel = new JScrollPane(tablaCampanias);
        
        // --- Añadir Listeners (Eventos) ---
        btnGuardar.addActionListener(e -> guardarCampania());
        btnEliminar.addActionListener(e -> eliminarCampania());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        tablaCampanias.getSelectionModel().addListSelectionListener(e -> mostrarSeleccionEnCampos());
        
        // --- Ensamblar el Frame ---
        add(pnlDatos, BorderLayout.NORTH);
        add(scrollPanel, BorderLayout.CENTER);
        add(pnlBotones, BorderLayout.SOUTH);

        pack(); // Ajusta el tamaño de la ventana a los componentes
        setLocationRelativeTo(null); // Centra la ventana
    }
    
    // ===================================================
    // Métodos de Lógica (Conexión al Servicio)
    // ===================================================

    /**
     * Carga los datos de todas las campañas en la JTable.
     */
    private void cargarDatosTabla() {
        try {
            List<Campania> lista = campaniaService.obtenerTodasCampanias();
            tablaModelo.setRowCount(0); // Limpiar filas
            
            for (Campania c : lista) {
                tablaModelo.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getTipoCampania(),
                    c.getFechaInicio(),
                    c.getFechaFin(),
                    c.getSupervisoresCargo(),
                    c.getDescripcionObjetivos()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda una nueva campaña o actualiza la existente.
     */
    private void guardarCampania() {
        try {
            Campania campania = new Campania();
            
            // Si el campo ID no está vacío, es una ACTUALIZACIÓN
            if (!txtId.getText().trim().isEmpty()) {
                campania.setId(Integer.parseInt(txtId.getText()));
            }

            // Mapeo de campos de texto a la entidad
            campania.setNombre(txtNombre.getText());
            campania.setTipoCampania(txtTipo.getText());
            
            // Manejo de fechas (Se requiere parseo y manejo de errores)
            campania.setFechaInicio(LocalDate.parse(txtFechaInicio.getText()));
            
            String fechaFinStr = txtFechaFin.getText();
            campania.setFechaFin(fechaFinStr.isEmpty() || fechaFinStr.equals("AAAA-MM-DD") ? null : LocalDate.parse(fechaFinStr));
            
            campania.setSupervisoresCargo(txtSupervisores.getText());
            campania.setDescripcionObjetivos(txtObjetivos.getText());
            // El estado se puede definir en el servicio o aquí.
            campania.setEstado("ACTIVA");

            // Llamada al servicio
            campaniaService.crearOActualizarCampania(campania);
            
            JOptionPane.showMessageDialog(this, "Campaña guardada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarDatosTabla();

        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use AAAA-MM-DD.", "Error de Entrada", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            // Captura errores de validación del Servicio (CampaniaService)
            JOptionPane.showMessageDialog(this, "Error de validación: " + e.getMessage(), "Error de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la campaña: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Elimina la campaña seleccionada en la tabla.
     */
    private void eliminarCampania() {
        int fila = tablaCampanias.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // El ID está en la columna 0 (oculta)
            int idCampania = (int) tablaCampanias.getValueAt(fila, 0); 
            
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la campaña " + idCampania + "?", 
                                                        "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (campaniaService.eliminarCampania(idCampania)) {
                    JOptionPane.showMessageDialog(this, "Campaña eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCampos();
                    cargarDatosTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la campaña (posiblemente no existe).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al intentar eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra los datos de la fila seleccionada en los campos de texto.
     */
    private void mostrarSeleccionEnCampos() {
        int fila = tablaCampanias.getSelectedRow();
        if (fila != -1) {
            txtId.setText(tablaCampanias.getValueAt(fila, 0).toString());
            txtNombre.setText(tablaCampanias.getValueAt(fila, 1).toString());
            txtTipo.setText(tablaCampanias.getValueAt(fila, 2).toString());
            txtFechaInicio.setText(tablaCampanias.getValueAt(fila, 3).toString());
            
            // Manejo de valores nulos o vacíos
            Object fechaFin = tablaCampanias.getValueAt(fila, 4);
            txtFechaFin.setText(fechaFin != null ? fechaFin.toString() : "AAAA-MM-DD");
            
            Object supervisores = tablaCampanias.getValueAt(fila, 5);
            txtSupervisores.setText(supervisores != null ? supervisores.toString() : "");
            
            Object objetivos = tablaCampanias.getValueAt(fila, 6);
            txtObjetivos.setText(objetivos != null ? objetivos.toString() : "");
        }
    }

    /**
     * Limpia todos los campos de entrada.
     */
    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtTipo.setText("");
        txtFechaInicio.setText("AAAA-MM-DD");
        txtFechaFin.setText("AAAA-MM-DD");
        txtSupervisores.setText("");
        txtObjetivos.setText("");
        tablaCampanias.clearSelection();
    }
    
    // Método principal para ejecutar la aplicación (opcional)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmGestionCampanias().setVisible(true);
        });
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

