package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CampaniaMySqlAdapter implements ICampaniaRepository {

    private static final String INSERT =
            "INSERT INTO campanias (nombre_campania, tipo_campania, fecha_inicio, fecha_fin, supervisores_cargo, descripcion_objetivos) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL =
            "SELECT * FROM campanias ORDER BY fecha_inicio DESC";

    private static final String UPDATE =
            "UPDATE campanias SET nombre_campania=?, tipo_campania=?, fecha_inicio=?, fecha_fin=?, supervisores_cargo=?, descripcion_objetivos=? " +
            "WHERE id_campania=?";

    private static final String DELETE =
            "DELETE FROM campanias WHERE id_campania=?";

    @Override
    public void guardar(Campania c) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            mapPreparedStatement(st, c);
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCampania(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al guardar campaña");
        }
    }

    @Override
    public List<Campania> listarTodos() {
        List<Campania> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al listar campañas");
        }

        return lista;
    }

    public void actualizar(Campania c) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(UPDATE)) {

            mapPreparedStatement(st, c);
            st.setLong(7, c.getIdCampania());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new CallCenterException("Error al actualizar campaña");
        }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(DELETE)) {

            st.setLong(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new CallCenterException("Error al eliminar campaña");
        }
    }

    private void mapPreparedStatement(PreparedStatement st, Campania c) throws SQLException {
        st.setString(1, c.getNombreCampania());
        st.setString(2, c.getTipoCampania());
        st.setDate(3, c.getFechaInicio() != null ? Date.valueOf(c.getFechaInicio()) : null);
        st.setDate(4, c.getFechaFin() != null ? Date.valueOf(c.getFechaFin()) : null);
        st.setString(5, c.getSupervisoresCargo());
        st.setString(6, c.getDescripcionObjetivos());
    }

   private Campania mapResultSet(ResultSet rs) throws SQLException {
    // CORRECCIÓN: Usamos el constructor vacío. 
    // No necesitamos 'dto' porque los datos vienen del 'rs' (ResultSet)
    Campania c = new Campania(); 
    
    // Aquí llenamos el objeto con los datos de la BD
    c.setIdCampania(rs.getLong("id_campania"));
    c.setNombreCampania(rs.getString("nombre_campania"));
    // Asegúrate que el nombre de la columna en BD sea correcto (ej: "descripcion" o "tipo_campania")
    c.setTipoCampania(rs.getString("tipo_campania")); 

    Date fi = rs.getDate("fecha_inicio");
    if (fi != null) c.setFechaInicio(fi.toLocalDate());

    Date ff = rs.getDate("fecha_fin");
    if (ff != null) c.setFechaFin(ff.toLocalDate());

    c.setSupervisoresCargo(rs.getString("supervisores_cargo"));
    c.setDescripcionObjetivos(rs.getString("descripcion_objetivos"));
    
    return c;
}

    @Override
    public Optional<Campania> buscarPorId(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
