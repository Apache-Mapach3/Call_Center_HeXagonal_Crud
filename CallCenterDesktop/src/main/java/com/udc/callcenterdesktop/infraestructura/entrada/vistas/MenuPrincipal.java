package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.dominio.puertos.entrada.*;
import java.awt.*;
import javax.swing.*;

public class MenuPrincipal extends JFrame {

    // 1. Declaramos los servicios que recibiremos del Main
    private final IAgenteService agenteService;
    private final IClienteService clienteService;
    private final ICampaniaService campaniaService;
    private final ILlamadaService llamadaService;

    // 2. Constructor: Recibe los servicios y construye la interfaz
    public MenuPrincipal(IAgenteService as, IClienteService cs, ICampaniaService camps, ILlamadaService ls) {
        this.agenteService = as;
        this.clienteService = cs;
        this.campaniaService = camps;
        this.llamadaService = ls;
        
        initUI();
    }

    private void initUI() {
        setTitle("Panel de Control - Call Center");
        setSize(500, 600); // Un poco más alto para que respire mejor
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Al cerrar este, se acaba el programa
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());

        // --- CABECERA ---
        JLabel lblTitulo = new JLabel("MENU PRINCIPAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // --- PANEL DE BOTONES (CENTRO) ---
        // GridLayout(4, 1) significa 4 filas, 1 columna
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 20, 20)); 
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 60, 40, 60)); // Márgenes laterales
        panelBotones.setBackground(new Color(245, 245, 245));

        // Botón 1: AGENTES (Azul)
        panelBotones.add(crearBotonMenu("GESTIÓN DE AGENTES", new Color(0, 102, 204), e -> {
            // Acción: Abrir ventana de Agentes
            new FrmGestionAgentes(agenteService).setVisible(true);
        }));

        // Botón 2: CLIENTES (Verde)
        panelBotones.add(crearBotonMenu("GESTIÓN DE CLIENTES", new Color(34, 139, 34), e -> {
            // Acción: Abrir ventana de Clientes
            new FrmGestionClientes(clienteService).setVisible(true);
        }));

        // Botón 3: CAMPAÑAS (Naranja)
        panelBotones.add(crearBotonMenu("GESTIÓN DE CAMPAÑAS", new Color(255, 140, 0), e -> {
            // Acción: Abrir ventana de Campañas
            new FrmGestionCampanias(campaniaService).setVisible(true);
        }));

        // Botón 4: LLAMADAS (Morado) - Este recibe TODOS los servicios
        panelBotones.add(crearBotonMenu("REGISTRO DE LLAMADAS", new Color(102, 51, 153), e -> {
            // Acción: Abrir ventana de Llamadas con acceso total
            new FrmRegistroLlamada(llamadaService, agenteService, clienteService, campaniaService).setVisible(true);
        }));

        add(panelBotones, BorderLayout.CENTER);

        // --- PIE DE PÁGINA ---
        JLabel lblStatus = new JLabel("Sistema Activo v2.0 - Arquitectura Hexagonal", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblStatus, BorderLayout.SOUTH);
    }

    // Método auxiliar para diseñar los botones (Estilo y colores)
    private JButton crearBotonMenu(String texto, Color color, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false); // Quita el recuadro feo al hacer clic
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Pone la manita al pasar el mouse
        btn.addActionListener(accion);
        
        // Borde doble para efecto visual elegante
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        return btn;
    }
}