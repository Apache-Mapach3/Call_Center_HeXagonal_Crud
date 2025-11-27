/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador de persistencia para la entidad Agente usando MySQL.
 * Implementa el patrón Repository para abstraer el acceso a datos.
 * 
 * <p>Este adaptador utiliza JDBC puro con PreparedStatements para
 * prevenir inyección SQL y gestionar eficientemente los recursos.</p>
 * 
 * <p><b>Características:</b></p>
 * <ul>
 *   <li>Uso de try-with-resources para gestión automática de conexiones</li>
 *   <li>PreparedStatements para prevenir SQL Injection</li>
 *   <li>Manejo centralizado de excepciones SQL</li>
 *   <li>Validaciones defensivas de parámetros</li>
 * </ul>
 * 
 * @author Jose 
 * @version 2.0
 * @since 2025
 */
public class AgenteMySqlAdapter implements IAgenteRepository {

    //  CONSTANTES SQL 
    
    private static final String SQL_INSERT = 
        "INSERT INTO agentes (nombre_completo, numero_empleado, telefono_contacto, " +
        "email, horario_turno, nivel_experiencia) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT id_agente, nombre_completo, numero_empleado, telefono_contacto, " +
        "email, horario_turno, nivel_experiencia " +
        "FROM agentes " +
        "ORDER BY nombre_completo";
    
    private static final String SQL_UPDATE = 
        "UPDATE agentes SET " +
        "nombre_completo = ?, " +
        "numero_empleado = ?, " +
        "telefono_contacto = ?, " +
        "email = ?, " +
        "horario_turno = ?, " +
        "nivel_experiencia = ? " +
        "WHERE id_agente = ?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM agentes WHERE id_agente = ?";

    /**
     * Persiste un nuevo agente en la base de datos.
     * 
     * @param agente entidad de dominio a guardar
     * @throws IllegalArgumentException si el agente es null
     * @throws CallCenterException si ocurre un error de persistencia
     */
    @Override
    public void guardar(Agente agente) {
        validarAgenteNoNulo(agente);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatementGuardar(stmt, agente);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "No se pudo guardar el agente en la base de datos"
                );
            }
            
            // Obtener el ID generado (opcional, útil para operaciones posteriores)
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    agente.setIdAgente(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al guardar agente en la base de datos: " + e.getMessage(), e
            );
        }
    }

    /**
     * Recupera todos los agentes registrados en el sistema.
     * 
     * @return lista inmutable de agentes
     * @throws CallCenterException si ocurre un error de lectura
     */
    @Override
    public List<Agente> listarTodos() {
        List<Agente> agentes = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                agentes.add(mapearAgente(rs));
            }
            
            return Collections.unmodifiableList(agentes);
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al consultar la lista de agentes: " + e.getMessage(), e
            );
        }
    }

    /**
     * Actualiza los datos de un agente existente.
     * 
     * @param agente entidad con datos actualizados (debe incluir ID)
     * @throws IllegalArgumentException si el agente es null o no tiene ID
     * @throws CallCenterException si el agente no existe o hay error de BD
     */
    @Override
    public void actualizar(Agente agente) {
        validarAgenteNoNulo(agente);
        validarIdAgente(agente.getIdAgente());
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            
            configurarStatementActualizar(stmt, agente);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "Agente con ID " + agente.getIdAgente() + " no encontrado para actualizar"
                );
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al actualizar agente: " + e.getMessage(), e
            );
        }
    }

    /**
     * Elimina un agente del sistema de forma permanente.
     * 
     * @param id identificador único del agente
     * @throws IllegalArgumentException si el ID es null o inválido
     * @throws CallCenterException si el agente no existe o hay error de BD
     */
    @Override
    public void eliminar(Long id) {
        validarIdAgente(id);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            
            stmt.setLong(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "Agente con ID " + id + " no encontrado para eliminar"
                );
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al eliminar agente: " + e.getMessage(), e
            );
        }
    }

    
    // MÉTODOS PRIVADOS DE UTILIDAD
   

    /**
     * Configura un PreparedStatement para operaciones de inserción.
     * 
     * @param stmt statement a configurar
     * @param agente datos origen
     * @throws SQLException si hay error al setear parámetros
     */
    private void configurarStatementGuardar(PreparedStatement stmt, Agente agente) 
            throws SQLException {
        stmt.setString(1, agente.getNombreCompleto());
        stmt.setString(2, agente.getNumeroEmpleado());
        stmt.setString(3, agente.getTelefonoContacto());
        stmt.setString(4, agente.getEmail());
        stmt.setString(5, agente.getHorarioTurno());
        stmt.setString(6, agente.getNivelExperiencia());
    }

    /**
     * Configura un PreparedStatement para operaciones de actualización.
     * 
     * @param stmt statement a configurar
     * @param agente datos origen
     * @throws SQLException si hay error al setear parámetros
     */
    private void configurarStatementActualizar(PreparedStatement stmt, Agente agente) 
            throws SQLException {
        configurarStatementGuardar(stmt, agente);
        stmt.setLong(7, agente.getIdAgente());
    }

    /**
     * Mapea un ResultSet a una entidad Agente.
     * 
     * @param rs resultado de consulta SQL
     * @return entidad Agente poblada
     * @throws SQLException si hay error al leer columnas
     */
    private Agente mapearAgente(ResultSet rs) throws SQLException {
        return new Agente(
            rs.getLong("id_agente"),
            rs.getString("nombre_completo"),
            rs.getString("numero_empleado"),
            rs.getString("telefono_contacto"),
            rs.getString("email"),
            rs.getString("horario_turno"),
            rs.getString("nivel_experiencia")
        );
    }

    /**
     * Valida que el agente no sea null.
     */
    private void validarAgenteNoNulo(Agente agente) {
        if (agente == null) {
            throw new IllegalArgumentException(
                "El agente no puede ser null"
            );
        }
    }

    /**
     * Valida que el ID del agente sea válido.
     */
    private void validarIdAgente(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                "ID de agente inválido: " + id
            );
        }
    }
}