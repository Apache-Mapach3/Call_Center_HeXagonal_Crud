/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;
/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
// Importamos nuestra Excepción Personalizada
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador MySQL.
 * YA NO muestra mensajes visuales (JOptionPane).
 * Si algo falla, lanza una CallCenterException para que la Vista decida qué hacer.
 */
public class AgenteMySqlAdapter implements IAgenteRepository {

    private static final String INSERT_SQL = "INSERT INTO agentes (nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM agentes";
    private static final String UPDATE_SQL = "UPDATE agentes SET nombre_completo=?, numero_empleado=?, telefono_contacto=?, email=?, horario_turno=?, nivel_experiencia=? WHERE id_agente=?";
    private static final String DELETE_SQL = "DELETE FROM agentes WHERE id_agente=?";

    @Override
    public void guardar(Agente agente) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            configurarStatement(stmt, agente);
            stmt.executeUpdate();
            
           
            
        } catch (SQLException e) {
            // Envolvemos el error técnico en uno de negocio
            throw new CallCenterException("No se pudo guardar el agente en la base de datos.", e);
        }
    }

    @Override
    public void actualizar(Agente agente) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            configurarStatement(stmt, agente);
            stmt.setLong(7, agente.getIdAgente());
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            // Lanzamos la excepción hacia arriba
            throw new CallCenterException("Error crítico al intentar actualizar el agente.", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            // Lanzamos la excepción hacia arriba
            throw new CallCenterException("No se pudo eliminar el agente. Verifique si tiene registros asociados.", e);
        }
    }

    @Override
    public List<Agente> listarTodos() {
        List<Agente> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Agente a = new Agente(
                    rs.getLong("id_agente"),
                    rs.getString("nombre_completo"),
                    rs.getString("numero_empleado"),
                    rs.getString("telefono_contacto"),
                    rs.getString("email"),
                    rs.getString("horario_turno"),
                    rs.getString("nivel_experiencia")
                );
                lista.add(a);
            }
            return lista;
            
        } catch (SQLException e) {
            // Lanzamos la excepción hacia arriba
            throw new CallCenterException("Error al intentar leer la lista de agentes.", e);
        }
    }

    // Método auxiliar privado
    private void configurarStatement(PreparedStatement stmt, Agente agente) throws SQLException {
        stmt.setString(1, agente.getNombreCompleto());
        stmt.setString(2, agente.getNumeroEmpleado());
        stmt.setString(3, agente.getTelefonoContacto());
        stmt.setString(4, agente.getEmail());
        stmt.setString(5, agente.getHorarioTurno());
        stmt.setString(6, agente.getNivelExperiencia());
    }
}