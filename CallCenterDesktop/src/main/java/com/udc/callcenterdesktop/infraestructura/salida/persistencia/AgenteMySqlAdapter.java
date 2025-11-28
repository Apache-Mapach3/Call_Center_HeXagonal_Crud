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
 * Adaptador de persistencia para la entidad Agente.
 * Implementa el patrón Repository para abstraer el acceso a datos.
 * * <p>Esta clase representa la implementación técnica del puerto de salida {@link IAgenteRepository}.
 * Aunque su nombre histórica refiere a MySQL, el código utiliza JDBC estándar,
 * por lo que es compatible con <b>SQLite</b> y otros motores relacionales.</p>
 * * <p><b>Buenas Prácticas Implementadas:</b></p>
 * <ul>
 * <li><b>Try-with-resources:</b> Garantiza el cierre de conexiones y evita fugas de memoria.</li>
 * <li><b>PreparedStatements:</b> Previene ataques de Inyección SQL.</li>
 * <li><b>Wrapping de Excepciones:</b> Captura errores técnicos (SQLException) y lanza errores de dominio (CallCenterException).</li>
 * <li><b>Consultas Explícitas:</b> Se definen las columnas a consultar en lugar de usar wildcard (*).</li>
 * </ul>
 * * @author Jose
 * @version 2.1
 * @since 2025
 */
public class AgenteMySqlAdapter implements IAgenteRepository {

 
    // CONSTANTES SQL (Consultas Precompiladas)

    
    private static final String SQL_INSERT = 
        "INSERT INTO agentes (nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT id_agente, nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia " +
        "FROM agentes ORDER BY nombre_completo";
    
    private static final String SQL_UPDATE = 
        "UPDATE agentes SET nombre_completo = ?, numero_empleado = ?, telefono_contacto = ?, " +
        "email = ?, horario_turno = ?, nivel_experiencia = ? WHERE id_agente = ?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM agentes WHERE id_agente = ?";
    
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id_agente, nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia " +
        "FROM agentes WHERE id_agente = ?";

    // IMPLEMENTACIÓN DE MÉTODOS CRUD

    /**
     * Persiste un nuevo agente en la base de datos.
     * Utiliza RETURN_GENERATED_KEYS para recuperar el ID asignado por SQLite.
     * * @param agente entidad de dominio a guardar
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
                throw new CallCenterException("No se pudo guardar el agente, no se creó el registro.");
            }
            
            // Recuperar el ID generado (Auto-increment)
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    agente.setIdAgente(generatedKeys.getLong(1));
                } else {
                    throw new CallCenterException("Error al obtener el ID del agente creado.");
                }
            }
            
        } catch (SQLException e) {
            // Manejo específico para duplicados (Constraint Unique)
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("Duplicate")) {
                throw new CallCenterException("El número de empleado ya se encuentra registrado.");
            }
            throw new CallCenterException("Error de base de datos al guardar agente: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera todos los agentes registrados en el sistema.
     * * @return lista inmutable de agentes
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
            
            // Retornamos lista inmutable para proteger los datos recuperados
            return Collections.unmodifiableList(agentes);
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al consultar la lista de agentes", e);
        }
    }

    /**
     * Actualiza los datos de un agente existente.
     * * @param agente entidad con datos actualizados (debe incluir ID)
     * @throws IllegalArgumentException si el agente es null o no tiene ID
     */
    @Override
    public void actualizar(Agente agente) {
        validarAgenteNoNulo(agente);
        validarIdAgente(agente.getIdAgente());
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            
            configurarStatementGuardar(stmt, agente); // Reusamos la config de parámetros
            stmt.setLong(7, agente.getIdAgente()); // El ID es el parámetro 7
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException("No se encontró el agente con ID " + agente.getIdAgente() + " para actualizar.");
            }
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al actualizar la información del agente", e);
        }
    }

    /**
     * Elimina un agente del sistema de forma permanente.
     * * @param id identificador único del agente
     */
    @Override
    public void eliminar(Long id) {
        validarIdAgente(id);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            
            stmt.setLong(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException("No se encontró el agente con ID " + id + " para eliminar.");
            }
            
        } catch (SQLException e) {
            // Manejo de restricción de clave foránea (Si el agente tiene llamadas)
            if (e.getMessage().contains("FOREIGN KEY")) {
                throw new CallCenterException("No se puede eliminar el agente porque tiene llamadas asociadas.");
            }
            throw new CallCenterException("Error al eliminar agente", e);
        }
    }
   
    public Agente buscarPorId(Long id) {
        validarIdAgente(id);
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
             
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAgente(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new CallCenterException("Error al buscar agente por ID", e);
        }
    }

    // MÉTODOS PRIVADOS DE UTILIDAD

    /**
     * Configura los parámetros del PreparedStatement.
     */
    private void configurarStatementGuardar(PreparedStatement stmt, Agente agente) throws SQLException {
        stmt.setString(1, agente.getNombreCompleto());
        stmt.setString(2, agente.getNumeroEmpleado());
        stmt.setString(3, agente.getTelefonoContacto());
        stmt.setString(4, agente.getEmail());
        stmt.setString(5, agente.getHorarioTurno());
        stmt.setString(6, agente.getNivelExperiencia());
    }

    /**
     * Mapea un ResultSet a una entidad Agente usando Setters.
     * Es más seguro que usar constructores largos.
     */
    private Agente mapearAgente(ResultSet rs) throws SQLException {
        Agente a = new Agente();
        a.setIdAgente(rs.getLong("id_agente"));
        a.setNombreCompleto(rs.getString("nombre_completo"));
        a.setNumeroEmpleado(rs.getString("numero_empleado"));
        a.setTelefonoContacto(rs.getString("telefono_contacto"));
        a.setEmail(rs.getString("email"));
        a.setHorarioTurno(rs.getString("horario_turno"));
        a.setNivelExperiencia(rs.getString("nivel_experiencia"));
        return a;
    }

    private void validarAgenteNoNulo(Agente agente) {
        if (agente == null) {
            throw new IllegalArgumentException("La entidad Agente no puede ser null.");
        }
    }

    private void validarIdAgente(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de agente inválido: " + id);
        }
    }
}