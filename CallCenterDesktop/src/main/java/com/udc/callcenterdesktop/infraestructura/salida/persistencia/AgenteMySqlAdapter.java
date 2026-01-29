package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AgenteMySqlAdapter implements IAgenteRepository {

    private static final String INSERT =
            "INSERT INTO agentes (nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL =
            "SELECT * FROM agentes ORDER BY nombre_completo";

    private static final String SELECT_BY_ID =
            "SELECT * FROM agentes WHERE id_agente = ?";

    private static final String UPDATE =
            "UPDATE agentes SET nombre_completo=?, numero_empleado=?, telefono_contacto=?, email=?, horario_turno=?, nivel_experiencia=? " +
            "WHERE id_agente=?";

    private static final String DELETE =
            "DELETE FROM agentes WHERE id_agente=?";

    @Override
    public void guardar(Agente a) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            mapPreparedStatement(st, a);
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setIdAgente(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al guardar agente: " + e.getMessage());
        }
    }

    @Override
    public List<Agente> listarTodos() {
        List<Agente> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al listar agentes");
        }

        return lista;
    }

    /**
     *
     * @param id
     * @return
     */

    public Optional<Agente> buscarPorId(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_BY_ID)) {

            st.setLong(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al buscar agente");
        }

        return Optional.empty();
    }

    @Override
    public void actualizar(Agente a) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(UPDATE)) {

            mapPreparedStatement(st, a);
            st.setLong(7, a.getIdAgente());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new CallCenterException("Error al actualizar agente");
        }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(DELETE)) {

            st.setLong(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new CallCenterException("Error al eliminar agente");
        }
    }

    private void mapPreparedStatement(PreparedStatement st, Agente a) throws SQLException {
        st.setString(1, a.getNombreCompleto());
        st.setString(2, a.getCodigoEmpleado());
        st.setString(3, a.getTelefono());
        st.setString(4, a.getEmail());
        st.setString(5, a.getTurno());
        st.setString(6, a.getExperiencia());
    }

    private Agente mapResultSet(ResultSet rs) throws SQLException {
        Agente a = new Agente();
        a.setIdAgente(rs.getLong("id_agente"));
        a.setNombreCompleto(rs.getString("nombre_completo"));
        a.setCodigoEmpleado(rs.getString("numero_empleado"));
        a.setTelefono(rs.getString("telefono_contacto"));
        a.setEmail(rs.getString("email"));
        a.setTurno(rs.getString("horario_turno"));
        a.setExperiencia(rs.getString("nivel_experiencia"));
        return a;
    }
}
