/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.AgenteMapper;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

/**
 * Servicio de Aplicación "Nivel Dios".
 * Implementa el patrón DTO para desacoplar totalmente la Vista del Dominio.
 * Realiza la conversión (Mapping) y aplica reglas de negocio.
 */
public class AgenteService implements IAgenteService {

    private final IAgenteRepository repositorio;

    public AgenteService(IAgenteRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void registrarAgente(AgenteDTO dto) {
        // Convertir DTO (Datos planos) a Entidad (Objeto de Negocio)
        Agente entidad = AgenteMapper.toEntity(dto);
        
        // Validar Reglas de Negocio sobre la Entidad
        if (validarDatos(entidad)) {
            // Guardar la Entidad
            repositorio.guardar(entidad);
        }
    }

    @Override
    public List<AgenteDTO> listarAgentes() {
        // Obtener lista de Entidades del repositorio
        List<Agente> entidades = repositorio.listarTodos();
        
        // Convertir Lista de Entidades a Lista de DTOs
        // Usamos Java Streams para hacerlo en una línea elegante
        return entidades.stream()
                .map(AgenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarAgente(AgenteDTO dto) {
        // Convertir
        Agente entidad = AgenteMapper.toEntity(dto);
        
        // Validar ID
        if (entidad.getIdAgente() == null || entidad.getIdAgente() <= 0) {
            throw new IllegalArgumentException("No se puede editar un agente sin ID.");
        }

        // Validar Datos y Actualizar
        if (validarDatos(entidad)) {
            repositorio.actualizar(entidad);
        }
    }

    @Override
    public void eliminarAgente(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID inválido para eliminación.");
        }
        
        // La confirmación visual (JOptionPane) se debería hacer en la Vista,
        // pero si prefieres mantener la lógica aquí, está bien por ahora.
        int confirmacion = JOptionPane.showConfirmDialog(null, 
                "¿Estás seguro de eliminar este agente?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            repositorio.eliminar(id);
        }
    }
    
    /**
     * Valida las reglas de negocio de la Entidad.
     */
    private boolean validarDatos(Agente agente) {
        if (agente.getNombreCompleto() == null || agente.getNombreCompleto().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "El nombre es obligatorio.");
            return false;
        }
        if (agente.getEmail() == null || !agente.getEmail().contains("@")) {
            JOptionPane.showMessageDialog(null, "El correo no es válido.");
            return false;
        }
        return true;
    }
}