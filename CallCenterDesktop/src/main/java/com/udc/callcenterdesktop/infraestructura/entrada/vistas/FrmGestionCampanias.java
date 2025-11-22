/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.entrada.vistas;


// IMPORTS DEL PROYECTO

import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException; // Importamos la excepción creada
import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO; // Si usas DTOs en la vista


// IMPORTS DE SWING Y UTILIDADES

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.time.LocalDate; // Para el manejo de fechas
import javax.swing.JFrame; // Si el archivo extiende JFrame

/**
 * Vista: Formulario para la Gestión de Campañas.
 * Clase que interactúa con el usuario y llama al Puerto de Entrada (ICampaniaService).
 * NOTA: Los nombres de componentes (jTableCampanias, txtId, etc.) son ejemplos.
 */
public class FrmGestionCampanias extends JFrame { // O JPanel, según tu implementación

    // Puerto de Entrada: Se inyecta la dependencia a través del constructor.
    private final ICampaniaService campaniaService;
    
    // Suponiendo que tienes un campo de texto para el ID
    private javax.swing.JTextField txtId; 
    private javax.swing.JTable jTableCampanias; 
    // ... otros componentes (txtNombre, txtTipo, etc.)

    /**
     * Constructor con Inyección de Dependencia.
     * @param campaniaService Implementación del servicio de campañas.
     */
    public FrmGestionCampanias(ICampaniaService campaniaService) {
        this.campaniaService = campaniaService;
        // initComponents(); // Método autogenerado por el IDE
        // cargarDatosTabla();
    }

    
    // 1. LÓGICA DE CARGA DE DATOS
    

    /**
     * Carga y muestra todas las campañas en la tabla.
     *  CORRECCIÓN: Usa 'obtenerTodasCampanias' que debe estar en el ICampaniaService.
     */
    private void cargarDatosTabla() {
        // Asegura que tienes el modelo de tabla correcto
        DefaultTableModel modelo = (DefaultTableModel) jTableCampanias.getModel();
        modelo.setRowCount(0); // Limpiar filas

        try {
            // Llama al método correcto del servicio
            List<Campania> listaCampanias = campaniaService.obtenerTodasCampanias();

            for (Campania c : listaCampanias) {
                modelo.addRow(new Object[]{
                    // El ID debe ser la primera columna
                    c.getId(), 
                    c.getNombre(),
                    c.getTipoCampania(),
                    c.getFechaInicio(),
                    c.getFechaFin(),
                    c.getSupervisoresCargo(),
                    c.getDescripcionObjetivos()
                });
            }
        } catch (CallCenterException e) {
            // Captura nuestra excepción personalizada
            JOptionPane.showMessageDialog(this, "Error de negocio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar campañas: " + e.getMessage(), "Error de Infraestructura", JOptionPane.ERROR_MESSAGE);
        }
    }
    
   
    // 2. LÓGICA DE GUARDAR / ACTUALIZAR
    

    /**
     * Manejador del botón Guardar.
     *CORRECCIÓN: Usa 'crearOActualizarCampania' y maneja la Entidad.
     */
    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        
        // Asumiendo que este método transforma los datos del formulario a una Entidad/DTO
        Campania campania = mapearFormularioACampania();
        
        if (campania == null) {
            JOptionPane.showMessageDialog(this, "Debe completar todos los campos requeridos.", "Validación", JOptionPane.WARNING_MESSAGE);
            return; 
        }
        
        try {
            boolean esActualizacion = campania.getId() > 0;
            
            // Llama al método unificado que está en el ICampaniaService
            campaniaService.crearOActualizarCampania(campania); 
            
            if (esActualizacion) {
                JOptionPane.showMessageDialog(this, "Campaña actualizada con éxito.");
            } else {
                JOptionPane.showMessageDialog(this, "Campaña registrada con éxito.");
            }
            
            limpiarCampos();
            cargarDatosTabla();

        } catch (CallCenterException e) {
            JOptionPane.showMessageDialog(this, "Error de negocio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error de Guardado", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    // 3. LÓGICA DE ELIMINACIÓN
   

    /**
     * Manejador del botón Eliminar.
     *CORRECCIÓN: Maneja la conversión de Long a int para el ID.
     */
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        int fila = jTableCampanias.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Object idObjeto = jTableCampanias.getValueAt(fila, 0);
            int idCampania;
            
            
            if (idObjeto instanceof Long) {
                idCampania = ((Long) idObjeto).intValue(); 
            } else if (idObjeto instanceof Integer) {
                idCampania = (int) idObjeto;
            } else {
                throw new IllegalArgumentException("El ID de la campaña no tiene un formato numérico válido.");
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                                                        "¿Seguro que desea eliminar la campaña ID: " + idCampania + "?", 
                                                        "Confirmar Eliminación", 
                                                        JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                campaniaService.eliminarCampania(idCampania); // Llama al servicio con el INT
                JOptionPane.showMessageDialog(this, "Campaña eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarDatosTabla();
            }
        } catch (CallCenterException e) {
            JOptionPane.showMessageDialog(this, "Error de negocio: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al intentar eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    // 4. MÉTODOS AUXILIARES (DEBES ASEGURARTE QUE EXISTEN)
    
    
    // Debes implementar tu lógica de mapeo. Aquí solo se define la firma:
    private Campania mapearFormularioACampania() {
       
        return new Campania(); // Retorna un objeto Campania real
    }
    
    private void limpiarCampos() {
        
    }
}
    
    
    
    
    
    
    
    
    
    

