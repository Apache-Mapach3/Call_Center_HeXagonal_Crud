/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Servicio de Aplicación (Core Lógico).
 * Orquestador que recibe peticiones de la vista, aplica reglas de negocio
 * y delega la persistencia al repositorio.
 */
public class AgenteService implements IAgenteService {

    // Inyección del Repositorio (Puerto de Salida)
    private final IAgenteRepository repositorio;

    public AgenteService(IAgenteRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarAgente(Agente agente) {
        // Validaciones de Negocio (Reglas antes de guardar)
        if (validarDatos(agente)) {
            // Si pasa la validación, guardamos
            repositorio.guardar(agente);
        }
    }

    @Override
    public List<Agente> listarAgentes() {
        // Simplemente delegamos la consulta al repositorio
        return repositorio.listarTodos();
    }

    @Override
    public void actualizarAgente(Agente agente) {
        // Validamos que el agente tenga un ID (es obligatorio para editar)
        if (agente.getIdAgente() == null || agente.getIdAgente() <= 0) {
            JOptionPane.showMessageDialog(null, "Error: No se puede editar un agente sin ID.");
            return;
        }

        if (validarDatos(agente)) {
            repositorio.actualizar(agente);
        }
    }

    @Override
    public void eliminarAgente(Long id) {
        if (id == null) {
            JOptionPane.showMessageDialog(null, "Error: ID inválido para eliminación.");
            return;
        }
        
        // Confirmación de seguridad antes de borrar
        int confirmacion = JOptionPane.showConfirmDialog(null, 
                "¿Estás seguro de eliminar este agente?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            repositorio.eliminar(id);
        }
    }
    
    /**
     * Método privado auxiliar para validar datos comunes.
     * Evita repetir if-else en registrar y actualizar.
     */
    private boolean validarDatos(Agente agente) {
        if (agente.getNombreCompleto().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre es obligatorio.");
            return false;
        }
        if (!agente.getEmail().contains("@")) {
            JOptionPane.showMessageDialog(null, "El correo no es válido.");
            return false;
        }
        return true;
    }
}