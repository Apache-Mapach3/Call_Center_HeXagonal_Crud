package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import javax.swing.*;
import java.awt.*;

public class FrmGestionAgentes extends JFrame {

    private final IAgenteService agenteService;

    // Constructor que recibe el servicio
    public FrmGestionAgentes(IAgenteService service) {
        this.agenteService = service;
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestión de Agentes");
        setSize(600, 450);
        setLocationRelativeTo(null); // Centrar en pantalla
        
        // IMPORTANTE: DISPOSE_ON_CLOSE cierra solo esta ventana, no toda la app
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel Principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Cabecera
        JLabel lblTitulo = new JLabel("Módulo de Agentes", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(0, 102, 204)); // Azul
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Cuerpo (Placeholder)
        JLabel lblInfo = new JLabel("<html><center>Aquí irán los formularios para:<br>Crear, Editar y Eliminar Agentes.<br><br>Servicio Inyectado: " + (agenteService != null ? "OK" : "NULL") + "</center></html>", SwingConstants.CENTER);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblInfo, BorderLayout.CENTER);

        add(panel);
    }
}