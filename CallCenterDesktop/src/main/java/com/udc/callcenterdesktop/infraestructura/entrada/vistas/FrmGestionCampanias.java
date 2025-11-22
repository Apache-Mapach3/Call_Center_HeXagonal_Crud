/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

public class FrmGestionCampanias extends JFrame {

    private ICampaniaService campaniaService;

    // Componentes Visuales
    private JTextField txtIdOculto;
    private JTextField txtNombre, txtSupervisor, txtFechaInicio, txtFechaFin, txtDescripcion;
    private JComboBox<String> cbTipo;
    private JButton btnGuardar, btnEliminar, btnLimpiar;
    private JTable tablaCampanias;
    private DefaultTableModel modeloTabla;

    // Fuentes
    private final Font fuenteLabels = new Font("Arial", Font.BOLD, 14);
    private final Font fuenteCampos = new Font("Arial", Font.PLAIN, 14);

    // Constructores
    public FrmGestionCampanias() {
        initUI();
    }

    public FrmGestionCampanias(ICampaniaService service) {
        this.campaniaService = service;
        initUI();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Gestión de Campañas - Call Center");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. TITULO (Encabezado Naranja/Rojo para diferenciar de Agentes)
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(255, 87, 34)); // Color Naranja Vibrante
        panelTitulo.setPreferredSize(new Dimension(900, 60));
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE CAMPAÑAS");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        panelTitulo.add(lblTitulo);

        // 2. FORMULARIO
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JPanel panelForm = new JPanel(new GridLayout(3, 4, 15, 15)); 
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos de la Campaña"),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        txtIdOculto = new JTextField(); txtIdOculto.setVisible(false);

        // Inicializar campos
        txtNombre = crearInput();
        txtSupervisor = crearInput();
        txtFechaInicio = crearInput(); // Formato YYYY-MM-DD
        txtFechaInicio.setToolTipText("Formato: YYYY-MM-DD");
        txtFechaFin = crearInput();
        txtDescripcion = crearInput();
        
        cbTipo = new JComboBox<>(new String[]{"Ventas Outbound", "Soporte Inbound", "Encuestas", "Cobranza"});
        cbTipo.setFont(fuenteCampos);
        cbTipo.setBackground(Color.WHITE);

        // Agregar al panel (Etiqueta -> Campo)
        panelForm.add(crearLabel("Nombre Campaña:")); panelForm.add(txtNombre);
        panelForm.add(crearLabel("Tipo:")); panelForm.add(cbTipo);
        panelForm.add(crearLabel("Supervisor:")); panelForm.add(txtSupervisor);
        panelForm.add(crearLabel("Fecha Inicio (YYYY-MM-DD):")); panelForm.add(txtFechaInicio);
        panelForm.add(crearLabel("Fecha Fin (YYYY-MM-DD):")); panelForm.add(txtFechaFin);
        panelForm.add(crearLabel("Descripción / Objetivo:")); panelForm.add(txtDescripcion);

        // 3. BOTONES
        JPanel panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnGuardar = crearBoton("GUARDAR CAMPAÑA", new Color(40, 167, 69));
        btnEliminar = crearBoton("ELIMINAR", new Color(220, 53, 69));
        btnLimpiar = crearBoton("LIMPIAR", new Color(108, 117, 125));

        btnGuardar.addActionListener((ActionEvent e) -> guardar());
        btnEliminar.addActionListener((ActionEvent e) -> eliminar());
        btnLimpiar.addActionListener((ActionEvent e) -> limpiar());

        panelBtn.add(btnGuardar);
        panelBtn.add(btnEliminar);
        panelBtn.add(btnLimpiar);

        panelSuperior.add(panelForm, BorderLayout.CENTER);
        panelSuperior.add(panelBtn, BorderLayout.SOUTH);

        // 4. TABLA
        String[] columnas = {"ID", "Nombre", "Tipo", "Inicio", "Fin", "Supervisor", "Descripción"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaCampanias = new JTable(modeloTabla);
        tablaCampanias.setRowHeight(25);
        tablaCampanias.setFont(new Font("Arial", Font.PLAIN, 13));
        tablaCampanias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tablaCampanias.getTableHeader().setBackground(new Color(240, 240, 240));

        JScrollPane scrollTabla = new JScrollPane(tablaCampanias);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Campañas Activas"));

        tablaCampanias.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { seleccionarFila(); }
        });

        add(panelTitulo, BorderLayout.NORTH);
        add(panelSuperior, BorderLayout.CENTER);
        add(scrollTabla, BorderLayout.SOUTH);
    }

    // --- MÉTODOS DE ESTILO ---
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(fuenteLabels);
        lbl.setHorizontalAlignment(SwingConstants.RIGHT);
        return lbl;
    }

    private JTextField crearInput() {
        JTextField txt = new JTextField();
        txt.setFont(fuenteCampos);
        txt.setBorder(BorderFactory.createCompoundBorder(
                txt.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return txt;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    // --- LÓGICA DE NEGOCIO ---

    private void cargarTabla() {
        if (campaniaService == null) return;
        modeloTabla.setRowCount(0);
        List<CampaniaDTO> lista = campaniaService.listarCampanias(); // Asumiendo que el método se llama así
        for (CampaniaDTO dto : lista) {
            modeloTabla.addRow(new Object[]{
                dto.idCampania, dto.nombreCampania, dto.tipoCampania, 
                dto.fechaInicio, dto.fechaFin, dto.supervisoresCargo, dto.descripcionObjetivos
            });
        }
    }

    private void guardar() {
        try {
            CampaniaDTO dto = new CampaniaDTO();
            dto.nombreCampania = txtNombre.getText();
            dto.tipoCampania = (String) cbTipo.getSelectedItem();
            dto.supervisoresCargo = txtSupervisor.getText();
            dto.descripcionObjetivos = txtDescripcion.getText();
            
            // Parseo de Fechas (Manejo simple)
            dto.fechaInicio = LocalDate.parse(txtFechaInicio.getText());
            dto.fechaFin = LocalDate.parse(txtFechaFin.getText());

            if (txtIdOculto.getText().isEmpty()) {
                campaniaService.registrarCampania(dto);
            } else {
                dto.idCampania = Long.parseLong(txtIdOculto.getText());
                campaniaService.actualizarCampania(dto);
            }
            
            JOptionPane.showMessageDialog(this, "✅ Campaña guardada exitosamente.");
            limpiar();
            cargarTabla();
            
        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "❌ Formato de fecha inválido. Use: YYYY-MM-DD (Ej: 2025-01-30)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void eliminar() {
        if (txtIdOculto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una campaña de la tabla.");
            return;
        }
        try {
            Long id = Long.parseLong(txtIdOculto.getText());
            campaniaService.eliminarCampania(id); // Asumiendo método eliminarCampania
            limpiar();
            cargarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
        }
    }

    private void seleccionarFila() {
        int fila = tablaCampanias.getSelectedRow();
        if (fila >= 0) {
            txtIdOculto.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
            cbTipo.setSelectedItem(modeloTabla.getValueAt(fila, 2).toString());
            txtFechaInicio.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtFechaFin.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtSupervisor.setText(modeloTabla.getValueAt(fila, 5).toString());
            txtDescripcion.setText(modeloTabla.getValueAt(fila, 6).toString());
        }
    }

    private void limpiar() {
        txtIdOculto.setText("");
        txtNombre.setText("");
        txtSupervisor.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtDescripcion.setText("");
        cbTipo.setSelectedIndex(0);
        tablaCampanias.clearSelection();
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

