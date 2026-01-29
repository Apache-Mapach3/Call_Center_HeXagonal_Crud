package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.dominio.puertos.entrada.*;
import javax.swing.*;
import java.awt.*;

public class FrmRegistroLlamada extends JFrame {

    // Necesitamos todos los servicios para registrar una llamada completa
    // (Ej: Seleccionar qué Agente atendió a qué Cliente en qué Campaña)
    private final ILlamadaService llamadaService;
    private final IAgenteService agenteService;
    private final IClienteService clienteService;
    private final ICampaniaService campaniaService;

    public FrmRegistroLlamada(ILlamadaService ls, IAgenteService as, IClienteService cs, ICampaniaService camps) {
        this.llamadaService = ls;
        this.agenteService = as;
        this.clienteService = cs;
        this.campaniaService = camps;
        initComponents();
    }

    private void initComponents() {
        setTitle("Registro de Llamadas");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Registro de Llamadas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(102, 51, 153)); // Morado
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Un pequeño panel informativo
        String info = "<html><center><h3>Formulario de Registro</h3>"
                + "Aquí se mostrarán ComboBox para seleccionar:<br>"
                + "- Agente (Desde AgenteService)<br>"
                + "- Cliente (Desde ClienteService)<br>"
                + "- Campaña (Desde CampaniaService)<br>"
                + "<br>Y se guardará usando LlamadaService.</center></html>";
        
        JLabel lblInfo = new JLabel(info, SwingConstants.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblInfo, BorderLayout.CENTER);

        add(panel);
    }
}