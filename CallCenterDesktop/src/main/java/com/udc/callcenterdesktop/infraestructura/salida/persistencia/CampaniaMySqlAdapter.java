package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador MySQL para Campaña - Versión KISS
 * SIMPLIFICADO: Implementación completa y funcional
 */
public class CampaniaMySqlAdapter implements ICampaniaRepository {

    private static final String INSERT =
            "INSERT INTO campanias (nombre_campania, tipo_campania, fecha_inicio, fecha_fin, " +
            "supervisores_cargo, descripcion_objetivos) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL =
            "SELECT * FROM campanias ORDER BY fecha_inicio DESC";

    private static final String SELECT_BY_ID =
            "SELECT * FROM campanias WHERE id_campania = ?";

    private static final String UPDATE =
            "UPDATE campanias SET nombre_campania=?, tipo_campania=?, fecha_inicio=?, " +
            "fecha_fin=?, supervisores_cargo=?, descripcion_objetivos=? WHERE id_campania=?";

    private static final String DELETE =
            "DELETE FROM campanias WHERE id_campania=?";

    @Override
    public void guardar(Campania c) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, c.getNombreCampania());
            st.setString(2, c.getTipoCampania());
            st.setDate(3, c.getFechaInicio() != null ? Date.valueOf(c.getFechaInicio()) : null);
            st.setDate(4, c.getFechaFin() != null ? Date.valueOf(c.getFechaFin()) : null);
            st.setString(5, c.getSupervisoresCargo());
            st.setString(6, c.getDescripcionObjetivos());

            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCampania(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al guardar campaña: " + e.getMessage());
        }
    }

    @Override
    public List<Campania> listarTodos() {
        List<Campania> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearResultSet(rs));
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al listar campañas: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public Optional<Campania> buscarPorId(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_BY_ID)) {

            st.setLong(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al buscar campaña: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(DELETE)) {

            st.setLong(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new CallCenterException("Error al eliminar campaña: " + e.getMessage());
        }
    }

    // Método auxiliar para convertir ResultSet -> Entidad
    private Campania mapearResultSet(ResultSet rs) throws SQLException {
        Campania c = new Campania();
        c.setIdCampania(rs.getLong("id_campania"));
        c.setNombreCampania(rs.getString("nombre_campania"));
        c.setTipoCampania(rs.getString("tipo_campania"));

        Date fi = rs.getDate("fecha_inicio");
        if (fi != null) c.setFechaInicio(fi.toLocalDate());

        Date ff = rs.getDate("fecha_fin");
        if (ff != null) c.setFechaFin(ff.toLocalDate());

        c.setSupervisoresCargo(rs.getString("supervisores_cargo"));
        c.setDescripcionObjetivos(rs.getString("descripcion_objetivos"));

        return c;
    }
}