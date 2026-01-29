package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import javax.swing.*;
import java.awt.*;

public class FrmGestionClientes extends JFrame {

    private final IClienteService clienteService;

    public FrmGestionClientes(IClienteService service) {
        this.clienteService = service;
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión de Clientes");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Módulo de Clientes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(34, 139, 34)); // Verde
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel lblInfo = new JLabel("<html><center>Gestión de base de datos de Clientes.<br>Conexión lista.<br><br>Servicio Inyectado: " + (clienteService != null ? "OK" : "NULL") + "</center></html>", SwingConstants.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblInfo, BorderLayout.CENTER);

        add(panel);
    }
}