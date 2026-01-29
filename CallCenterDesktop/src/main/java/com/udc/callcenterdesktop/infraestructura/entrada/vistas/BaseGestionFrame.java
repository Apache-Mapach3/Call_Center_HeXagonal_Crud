package com.udc.callcenterdesktop.infraestructura.entrada.vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class BaseGestionFrame<T> extends JInternalFrame {
    protected JPanel pFormulario;
    protected DefaultTableModel modelo;
    protected JTable tabla;

    public BaseGestionFrame(String titulo, String[] columnas, Color colorFondo) {
        super(titulo, true, true, true, true);
        setSize(800, 600);
        setLayout(new BorderLayout());

        pFormulario = new JPanel();
        pFormulario.setBackground(colorFondo);
        add(pFormulario, BorderLayout.NORTH);

        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        
        JPanel pBotones = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarRegistro());
        pBotones.add(btnGuardar);
        add(pBotones, BorderLayout.SOUTH);
    }

    protected abstract void configurarFormulario();
    protected abstract void guardarRegistro();
    protected abstract void cargarDatos();
    protected abstract void limpiarCampos();
    protected abstract void eliminarRegistro();

    protected void msg(String s) { JOptionPane.showMessageDialog(this, s); }
    protected void error(String s) { JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE); }
}