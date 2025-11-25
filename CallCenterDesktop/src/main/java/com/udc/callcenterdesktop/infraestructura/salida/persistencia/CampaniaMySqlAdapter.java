/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CampaniaMySqlAdapter implements ICampaniaRepository {

    private static final String INSERT_SQL = "INSERT INTO campanias (nombre_campania, tipo_campania, fecha_inicio, fecha_fin, supervisores_cargo, descripcion_objetivos) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM campanias";
    private static final String UPDATE_SQL = "UPDATE campanias SET nombre_campania=?, tipo_campania=?, fecha_inicio=?, fecha_fin=?, supervisores_cargo=?, descripcion_objetivos=? WHERE id_campania=?";
    private static final String DELETE_SQL = "DELETE FROM campanias WHERE id_campania=?";

    @Override
    public void guardar(Campania c) {
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            configurarStatement(stmt, c);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new CallCenterException("Error BD al guardar campaña", e); }
    }

    @Override
    public List<Campania> listarTodos() {
        List<Campania> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Campania(
                    rs.getLong("id_campania"), 
                    rs.getString("nombre_campania"), 
                    rs.getString("tipo_campania"),
                    // CORRECCIÓN - método seguro para fechas
                    obtenerFecha(rs, "fecha_inicio"), 
                    obtenerFecha(rs, "fecha_fin"),
                    rs.getString("supervisores_cargo"), 
                    rs.getString("descripcion_objetivos")
                ));
            }
            return lista;
        } catch (SQLException e) { throw new CallCenterException("Error BD al listar campañas", e); }
    }

    @Override
    public void actualizar(Campania c) {
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            configurarStatement(stmt, c);
            stmt.setLong(7, c.getIdCampania());
            stmt.executeUpdate();
        } catch (SQLException e) { throw new CallCenterException("Error BD al actualizar campaña", e); }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) { throw new CallCenterException("Error BD al eliminar campaña", e); }
    }

    // MÉTODOS AUXILIARES

    private void configurarStatement(PreparedStatement stmt, Campania c) throws SQLException {
        stmt.setString(1, c.getNombreCampania());
        stmt.setString(2, c.getTipoCampania());
        
        // Manejo seguro de Fecha Inicio
        if (c.getFechaInicio() != null) stmt.setDate(3, Date.valueOf(c.getFechaInicio()));
        else stmt.setNull(3, Types.DATE);

        // Manejo seguro de Fecha Fin
        if (c.getFechaFin() != null) stmt.setDate(4, Date.valueOf(c.getFechaFin()));
        else stmt.setNull(4, Types.DATE);

        stmt.setString(5, c.getSupervisoresCargo());
        stmt.setString(6, c.getDescripcionObjetivos());
    }

    // Método para convertir java.sql.Date en java.time.LocalDate sin explotar por NULLs
    private LocalDate obtenerFecha(ResultSet rs, String columna) throws SQLException {
        Date fechaSql = rs.getDate(columna);
        if (fechaSql != null) {
            return fechaSql.toLocalDate();
        }
        return null; // Si es null en BD devolvemos null en Java
    }
}