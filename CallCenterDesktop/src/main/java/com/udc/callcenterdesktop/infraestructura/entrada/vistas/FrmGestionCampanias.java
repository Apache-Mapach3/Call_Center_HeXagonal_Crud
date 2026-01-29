package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import javax.swing.*;
import java.awt.*;

public class FrmGestionCampanias extends JFrame {

    private final ICampaniaService campaniaService;

    public FrmGestionCampanias(ICampaniaService service) {
        this.campaniaService = service;
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión de Campañas");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Módulo de Campañas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(255, 140, 0)); // Naranja
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        JLabel lblInfo = new JLabel("<html><center>Configuración de Campañas de Marketing.<br><br>Servicio Inyectado: " + (campaniaService != null ? "OK" : "NULL") + "</center></html>", SwingConstants.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblInfo, BorderLayout.CENTER);

        add(panel);
    }
}