package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.*;
import com.udc.callcenterdesktop.dominio.puertos.entrada.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FrmRegistroLlamada extends JFrame {

    private final ILlamadaService llamadaService;
    private final IAgenteService agenteService;
    private final IClienteService clienteService;
    private final ICampaniaService campaniaService;
    
    private JComboBox<AgenteDTO> cmbAgente;
    private JComboBox<ClienteDTO> cmbCliente;
    private JComboBox<CampaniaDTO> cmbCampania;
    private JTextField txtDuracion;
    private JComboBox<String> cmbResultado;
    private JTextArea txtObservaciones;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnGuardar, btnLimpiar;

    public FrmRegistroLlamada(ILlamadaService ls, IAgenteService as, IClienteService cs, ICampaniaService camps) {
        this.llamadaService = ls;
        this.agenteService = as;
        this.clienteService = cs;
        this.campaniaService = camps;
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setTitle("Registro de Llamadas");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== CABECERA =====
        JLabel lblTitulo = new JLabel("Registro de Llamadas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(0, 102, 204));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // ===== FORMULARIO =====
        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelFormulario.setBackground(new Color(245, 240, 255));

        panelFormulario.add(new JLabel("Agente:"));
        cmbAgente = new JComboBox<>();
        cargarAgentes();
        panelFormulario.add(cmbAgente);

        panelFormulario.add(new JLabel("Cliente:"));
        cmbCliente = new JComboBox<>();
        cargarClientes();
        panelFormulario.add(cmbCliente);

        panelFormulario.add(new JLabel("Campaña:"));
        cmbCampania = new JComboBox<>();
        cargarCampanias();
        panelFormulario.add(cmbCampania);

        panelFormulario.add(new JLabel("Duración (segundos):"));
        txtDuracion = new JTextField();
        panelFormulario.add(txtDuracion);

        panelFormulario.add(new JLabel("Resultado:"));
        cmbResultado = new JComboBox<>(new String[]{"Venta Exitosa", "No Contesta", "Interesado", "No Interesado", "Reagendar"});
        panelFormulario.add(cmbResultado);

        panelFormulario.add(new JLabel("Observaciones:"));
        txtObservaciones = new JTextArea(3, 20);
        panelFormulario.add(new JScrollPane(txtObservaciones));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Registrar Llamada");
        btnGuardar.setBackground(new Color(0, 153, 76));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> registrarLlamada());

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnLimpiar);
        panelFormulario.add(panelBotones);

        add(panelFormulario, BorderLayout.WEST);

        // ===== TABLA =====
        String[] columnas = {"ID", "Fecha/Hora", "Agente", "Cliente", "Campaña", "Duración", "Resultado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modeloTabla);
        
        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);
    }

    private void cargarAgentes() {
        try {
            cmbAgente.removeAllItems();
            for (AgenteDTO agente : agenteService.listarTodos()) {
                cmbAgente.addItem(agente);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar agentes: " + ex.getMessage());
        }
    }

    private void cargarClientes() {
        try {
            cmbCliente.removeAllItems();
            for (ClienteDTO cliente : clienteService.listarTodos()) {
                cmbCliente.addItem(cliente);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + ex.getMessage());
        }
    }

    private void cargarCampanias() {
        try {
            cmbCampania.removeAllItems();
            for (CampaniaDTO campania : campaniaService.listarTodas()) {
                cmbCampania.addItem(campania);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar campañas: " + ex.getMessage());
        }
    }

    private void registrarLlamada() {
        try {
            LlamadaDTO dto = new LlamadaDTO();
            dto.setFechaHora(LocalDateTime.now());
            dto.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
            dto.setDetalleResultado((String) cmbResultado.getSelectedItem());
            dto.setObservaciones(txtObservaciones.getText().trim());
            
            AgenteDTO agente = (AgenteDTO) cmbAgente.getSelectedItem();
            ClienteDTO cliente = (ClienteDTO) cmbCliente.getSelectedItem();
            CampaniaDTO campania = (CampaniaDTO) cmbCampania.getSelectedItem();
            
            dto.setIdAgente(agente.getIdAgente());
            dto.setIdCliente(cliente.getIdCliente());
            dto.setIdCampania(campania.getIdCampania());

            llamadaService.registrar(dto);
            JOptionPane.showMessageDialog(this, "Llamada registrada correctamente");
            limpiarCampos();
            cargarDatos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        try {
            for (LlamadaDTO llamada : llamadaService.listarLlamadas()) {
                modeloTabla.addRow(new Object[]{
                    llamada.getIdLlamada(),
                    llamada.getFechaHora().format(fmt),
                    llamada.getNombreAgente(),
                    llamada.getNombreCliente(),
                    llamada.getNombreCampania(),
                    llamada.getDuracionFormateada(),
                    llamada.getDetalleResultado()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        txtDuracion.setText("");
        txtObservaciones.setText("");
        cmbResultado.setSelectedIndex(0);
        if (cmbAgente.getItemCount() > 0) cmbAgente.setSelectedIndex(0);
        if (cmbCliente.getItemCount() > 0) cmbCliente.setSelectedIndex(0);
        if (cmbCampania.getItemCount() > 0) cmbCampania.setSelectedIndex(0);
    }
}