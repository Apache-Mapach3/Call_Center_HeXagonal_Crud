/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.*;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.puertos.entrada.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmRegistroLlamada extends JFrame {

    // Necesitamos 4 servicios para llenar los combos y registrar
    private final ILlamadaService llamadaService;
    private final IAgenteService agenteService;
    private final IClienteService clienteService;
    private final ICampaniaService campaniaService;

    private JComboBox<ItemCombo> cbAgente, cbCliente, cbCampania;
    private JTextField txtDuracion;
    private JTextArea txtDetalle, txtObservacion;
    private JTable tablaHistorial;
    private DefaultTableModel modelo;

    public FrmRegistroLlamada(ILlamadaService ls, IAgenteService as, IClienteService cs, ICampaniaService camps) {
        this.llamadaService = ls;
        this.agenteService = as;
        this.clienteService = cs;
        this.campaniaService = camps;
        initUI();
        cargarCombos();
        cargarTabla();
    }

    private void initUI() {
        setTitle("Registro de Llamadas");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel pHeader = new JPanel(); pHeader.setBackground(new Color(102, 51, 153));
        JLabel lblTitulo = new JLabel("REGISTRO Y CONTROL DE LLAMADAS");
        lblTitulo.setForeground(Color.WHITE); lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        pHeader.add(lblTitulo); add(pHeader, BorderLayout.NORTH);

        JPanel pForm = new JPanel(new GridLayout(3, 4, 15, 15));
        pForm.setBorder(BorderFactory.createTitledBorder("Detalles"));

        cbAgente = new JComboBox<>(); cbCliente = new JComboBox<>();
        cbCampania = new JComboBox<>(); txtDuracion = new JTextField();
        txtDetalle = new JTextArea(3, 20); txtObservacion = new JTextArea(3, 20);

        pForm.add(new JLabel("Agente:")); pForm.add(cbAgente);
        pForm.add(new JLabel("Cliente:")); pForm.add(cbCliente);
        pForm.add(new JLabel("Campaña:")); pForm.add(cbCampania);
        pForm.add(new JLabel("Duración (seg):")); pForm.add(txtDuracion);
        pForm.add(new JLabel("Resultado:")); pForm.add(new JScrollPane(txtDetalle));
        pForm.add(new JLabel("Observaciones:")); pForm.add(new JScrollPane(txtObservacion));

        JButton btnGuardar = new JButton("REGISTRAR LLAMADA");
        btnGuardar.setBackground(new Color(102, 51, 153)); btnGuardar.setForeground(Color.WHITE);
        btnGuardar.addActionListener(e -> guardar());

        JPanel pC = new JPanel(new BorderLayout()); pC.add(pForm, BorderLayout.CENTER); pC.add(btnGuardar, BorderLayout.SOUTH);
        add(pC, BorderLayout.CENTER);

        modelo = new DefaultTableModel(null, new String[]{"ID", "Fecha", "Agente", "Cliente", "Campaña", "Resultado"});
        tablaHistorial = new JTable(modelo);
        add(new JScrollPane(tablaHistorial), BorderLayout.SOUTH);
    }

    private void cargarCombos() {
        try {
            cbAgente.removeAllItems();
            for (AgenteDTO a : agenteService.listarAgentes()) cbAgente.addItem(new ItemCombo(a.id, a.nombre));

            cbCliente.removeAllItems();
            if (clienteService != null) { // Validación por si el servicio aún no está inyectado
                 for (ClienteDTO c : clienteService.listarClientes()) cbCliente.addItem(new ItemCombo(c.idCliente, c.nombreCompleto));
            }

            cbCampania.removeAllItems();
            for (CampaniaDTO c : campaniaService.listarCampanias()) cbCampania.addItem(new ItemCombo(c.idCampania, c.nombreCampania));
        } catch (Exception e) {
            // Ignorar errores silenciosos si faltan servicios al inicio
        }
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        List<LlamadaDTO> lista = llamadaService.listarHistorial();
        for (LlamadaDTO l : lista) {
            modelo.addRow(new Object[]{l.idLlamada, l.fechaHora, l.nombreAgente, l.nombreCliente, l.nombreCampania, l.detalleResultado});
        }
    }

    private void guardar() {
        try {
            LlamadaDTO dto = new LlamadaDTO();
            dto.fechaHora = LocalDateTime.now();
            dto.duracion = Integer.parseInt(txtDuracion.getText());
            dto.detalleResultado = txtDetalle.getText();
            dto.observaciones = txtObservacion.getText();

            ItemCombo ag = (ItemCombo) cbAgente.getSelectedItem();
            ItemCombo cl = (ItemCombo) cbCliente.getSelectedItem();
            ItemCombo cp = (ItemCombo) cbCampania.getSelectedItem();

            if (ag != null) dto.idAgente = ag.id;
            if (cl != null) dto.idCliente = cl.id;
            if (cp != null) dto.idCampania = cp.id;

            llamadaService.registrarLlamada(dto);
            JOptionPane.showMessageDialog(this, "Llamada registrada.");
            cargarTabla();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La duración debe ser un número.");
        } catch (CallCenterException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private class ItemCombo {
        Long id; String texto;
        public ItemCombo(Long id, String t) { this.id = id; this.texto = t; }
        public String toString() { return texto; }
    }
}