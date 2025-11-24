/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.dominio.puertos.entrada.*;
import java.awt.*;
import javax.swing.*;

public class MenuPrincipal extends JFrame {

    // Usamos INTERFACES (La I al principio es clave)
    private final IAgenteService agenteService;
    private final IClienteService clienteService;
    private final ICampaniaService campaniaService;
    private final ILlamadaService llamadaService;

    // Constructor corregido para recibir Interfaces
    public MenuPrincipal(IAgenteService as, IClienteService cs, ICampaniaService camps, ILlamadaService ls) {
        this.agenteService = as;
        this.clienteService = cs;
        this.campaniaService = camps;
        this.llamadaService = ls;
        initUI();
    }

    private void initUI() {
        setTitle("Sistema Call Center");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JButton btn1 = new JButton("Agentes");
        btn1.addActionListener(e -> new FrmGestionAgentes(agenteService).setVisible(true));
        
        JButton btn2 = new JButton("Clientes");
        btn2.addActionListener(e -> {
             if(clienteService!=null) new FrmGestionClientes(clienteService).setVisible(true);
        });
        
        JButton btn3 = new JButton("Campañas");
        btn3.addActionListener(e -> new FrmGestionCampanias(campaniaService).setVisible(true));
        
        JButton btn4 = new JButton("Llamadas");
        btn4.addActionListener(e -> {
            if(llamadaService!=null) new FrmRegistroLlamada(llamadaService, agenteService, clienteService, campaniaService).setVisible(true);
        });

        add(btn1); add(btn2); add(btn3); add(btn4);
    }
}