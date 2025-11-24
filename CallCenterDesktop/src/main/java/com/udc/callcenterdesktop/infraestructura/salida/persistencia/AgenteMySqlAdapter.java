/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;
/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgenteMySqlAdapter implements IAgenteRepository {

    private static final String INSERT = "INSERT INTO agentes (nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM agentes";
    private static final String UPDATE = "UPDATE agentes SET nombre_completo=?, numero_empleado=?, telefono_contacto=?, email=?, horario_turno=?, nivel_experiencia=? WHERE id_agente=?";
    private static final String DELETE = "DELETE FROM agentes WHERE id_agente=?";

    @Override
    public void guardar(Agente a) {
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(INSERT)) {
            configurar(stmt, a);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new CallCenterException("Error BD al guardar agente", e); }
    }

    @Override
    public List<Agente> listarTodos() {
        List<Agente> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(SELECT); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Agente(
                    rs.getLong("id_agente"), rs.getString("nombre_completo"), rs.getString("numero_empleado"),
                    rs.getString("telefono_contacto"), rs.getString("email"), rs.getString("horario_turno"),
                    rs.getString("nivel_experiencia")
                ));
            }
            return lista;
        } catch (SQLException e) { throw new CallCenterException("Error BD al listar agentes", e); }
    }

    @Override
    public void actualizar(Agente a) {
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            configurar(stmt, a);
            stmt.setLong(7, a.getIdAgente());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new CallCenterException("Error BD al actualizar agente", e); }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(DELETE)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new CallCenterException("Error BD al eliminar agente", e); }
    }

    private void configurar(PreparedStatement stmt, Agente a) throws SQLException {
        stmt.setString(1, a.getNombreCompleto());
        stmt.setString(2, a.getNumeroEmpleado());
        stmt.setString(3, a.getTelefonoContacto());
        stmt.setString(4, a.getEmail());
        stmt.setString(5, a.getHorarioTurno());
        stmt.setString(6, a.getNivelExperiencia());
    }
}