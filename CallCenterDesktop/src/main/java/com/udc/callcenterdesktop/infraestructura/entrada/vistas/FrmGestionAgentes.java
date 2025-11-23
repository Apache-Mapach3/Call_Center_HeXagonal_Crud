/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.aplicacion.servicios.AgenteService;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class FrmGestionAgentes extends JFrame {

    private AgenteService agenteService;
    
    // Componentes
    private JTextField txtIdOculto;
    private JTextField txtNombre, txtNumeroEmpleado, txtTelefono, txtEmail;
    private JComboBox<String> cbHorario, cb