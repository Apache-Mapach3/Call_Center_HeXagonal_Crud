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

/**
 * Adaptador de persistencia para Campañas en MySQL Server.
 * 
 * <p><b>Optimizaciones MySQL:</b></p>
 * <ul>
 *   <li>Uso de tipo DATE nativo de MySQL</li>
 *   <li>Conversión automática LocalDate ↔ java.sql.Date</li>
 *   <li>Manejo correcto de valores NULL en fechas</li>
 * </ul>
 * 
 * @author Carlos
 * @version 4.0 - MySQL Edition
 */
public class CampaniaMySqlAdapter implements ICampaniaRepository {

    // Consultas SQL optimizadas para MySQL
    private static final String INSERT_SQL = 
        "INSERT INTO campanias (nombre_campania, tipo_campania, fecha_inicio, fecha_fin, " +
        "supervisores_cargo, descripcion_objetivos) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT id_campania, nombre_campania, tipo_campania, fecha_inicio, fecha_fin, " +
        "supervisores_cargo, descripcion_objetivos FROM campanias ORDER BY fecha_inicio DESC";
    
    private static final String UPDATE_SQL = 
        "UPDATE campanias SET nombre_campania=?, tipo_campania=?, fecha_inicio=?, " +
        "fecha_fin=?, supervisores_cargo=?, descripcion_objetivos=? WHERE id_campania=?";
    
    private static final String DELETE_SQL = 
        "DELETE FROM campanias WHERE id_campania=?";

    @Override
    public void guardar(Campania c) {
        if (c == null) {
            throw new IllegalArgumentException("La campaña no puede ser null");
        }
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatementCampania(stmt, c);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CallCenterException("No se pudo guardar la campaña, no se afectaron filas.");
            }

            // Recuperar ID generado por MySQL
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    c.setIdCampania(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            // Manejo de constraint UNIQUE (nombre_campania)
            if (e.getErrorCode() == 1062) { // MySQL error code para duplicate entry
                throw new CallCenterException(
                    "Ya existe una campaña con el nombre: " + c.getNombreCampania()
                );
            }
            throw new CallCenterException("Error de BD al guardar campaña", e); 
        }
    }

    @Override
    public List<Campania> listarTodos() {
        List<Campania> lista = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearCampania(rs));
            }
            return lista;
            
        } catch (SQLException e) { 
            throw new CallCenterException("Error de BD al listar campañas", e); 
        }
    }

    @Override
    public void actualizar(Campania c) {
        if (c == null || c.getIdCampania() == null) {
            throw new IllegalArgumentException("La campaña y su ID no pueden ser null");
        }
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            configurarStatementCampania(stmt, c);
            stmt.setLong(7, c.getIdCampania());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CallCenterException(
                    "No se encontró la campaña con ID: " + c.getIdCampania()
                );
            }
            
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                throw new CallCenterException(
                    "Ya existe otra campaña con el nombre: " + c.getNombreCampania()
                );
            }
            throw new CallCenterException("Error de BD al actualizar campaña", e); 
        }
    }

    @Override
    public void eliminar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para eliminar");
        }
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CallCenterException("No se encontró la campaña con ID: " + id);
            }
            
        } catch (SQLException e) {
            // Error de Foreign Key (hay llamadas asociadas)
            if (e.getErrorCode() == 1451) { // Cannot delete or update a parent row
                throw new CallCenterException(
                    "No se puede eliminar la campaña porque tiene llamadas asociadas"
                );
            }
            throw new CallCenterException("Error de BD al eliminar campaña", e); 
        }
    }

    /**
     * Configura los parámetros del PreparedStatement.
     * IMPORTANTE: Conversión correcta de LocalDate a java.sql.Date para MySQL.
     */
    private void configurarStatementCampania(PreparedStatement stmt, Campania c) throws SQLException {
        stmt.setString(1, c.getNombreCampania());
        stmt.setString(2, c.getTipoCampania());
        
        // CONVERSIÓN CORRECTA PARA MYSQL: LocalDate → java.sql.Date
        if (c.getFechaInicio() != null) {
            stmt.setDate(3, java.sql.Date.valueOf(c.getFechaInicio()));
        } else {
            stmt.setNull(3, Types.DATE);
        }
        
        if (c.getFechaFin() != null) {
            stmt.setDate(4, java.sql.Date.valueOf(c.getFechaFin()));
        } else {
            stmt.setNull(4, Types.DATE);
        }
        
        stmt.setString(5, c.getSupervisoresCargo());
        stmt.setString(6, c.getDescripcionObjetivos());
    }
    
    /**
     * Mapea un ResultSet a una entidad Campaña.
     * IMPORTANTE: Conversión correcta de java.sql.Date a LocalDate.
     */
    private Campania mapearCampania(ResultSet rs) throws SQLException {
        Campania c = new Campania();
        c.setIdCampania(rs.getLong("id_campania"));
        c.setNombreCampania(rs.getString("nombre_campania"));
        c.setTipoCampania(rs.getString("tipo_campania"));
        
        // CONVERSIÓN CORRECTA PARA MYSQL: java.sql.Date → LocalDate
        java.sql.Date fechaInicioSql = rs.getDate("fecha_inicio");
        if (fechaInicioSql != null) {
            c.setFechaInicio(fechaInicioSql.toLocalDate());
        }
        
        java.sql.Date fechaFinSql = rs.getDate("fecha_fin");
        if (fechaFinSql != null) {
            c.setFechaFin(fechaFinSql.toLocalDate());
        }
        
        c.setSupervisoresCargo(rs.getString("supervisores_cargo"));
        c.setDescripcionObjetivos(rs.getString("descripcion_objetivos"));
        
        return c;
    }
}