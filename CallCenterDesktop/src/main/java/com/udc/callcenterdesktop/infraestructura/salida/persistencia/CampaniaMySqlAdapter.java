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

    // Consultas SQL estándar
    private static final String INSERT_SQL = "INSERT INTO campanias (nombre_campania, tipo_campania, fecha_inicio, fecha_fin, supervisores_cargo, descripcion_objetivos) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM campanias";
    private static final String UPDATE_SQL = "UPDATE campanias SET nombre_campania=?, tipo_campania=?, fecha_inicio=?, fecha_fin=?, supervisores_cargo=?, descripcion_objetivos=? WHERE id_campania=?";
    private static final String DELETE_SQL = "DELETE FROM campanias WHERE id_campania=?";

    @Override
    public void guardar(Campania c) {
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatement(stmt, c);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new CallCenterException("No se pudo guardar la campaña, no se afectaron filas.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    c.setIdCampania(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) { 
            throw new CallCenterException("Error BD al guardar campaña", e); 
        }
    }

    @Override
    public List<Campania> listarTodos() {
        List<Campania> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                
                String fechaFinStr = rs.getString("fecha_fin");
                LocalDate fechaFin = (fechaFinStr != null && !fechaFinStr.isEmpty()) ? LocalDate.parse(fechaFinStr) : null;

                lista.add(new Campania(
                    rs.getLong("id_campania"), 
                    rs.getString("nombre_campania"), 
                    rs.getString("tipo_campania"),
                    LocalDate.parse(rs.getString("fecha_inicio")), 
                    fechaFin,
                    rs.getString("supervisores_cargo"), 
                    rs.getString("descripcion_objetivos")
                ));
            }
            return lista;
        } catch (SQLException e) { 
            throw new CallCenterException("Error BD al listar campañas", e); 
        }
    }

    @Override
    public void actualizar(Campania c) {
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            configurarStatement(stmt, c);
            stmt.setLong(7, c.getIdCampania());
            stmt.executeUpdate();
            
        } catch (SQLException e) { 
            throw new CallCenterException("Error BD al actualizar campaña", e); 
        }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) { 
            throw new CallCenterException("Error BD al eliminar campaña", e); 
        }
    }

    

    private void configurarStatement(PreparedStatement stmt, Campania c) throws SQLException {
        stmt.setString(1, c.getNombreCampania());
        stmt.setString(2, c.getTipoCampania());
        
        
        stmt.setString(3, c.getFechaInicio().toString());

        
        if (c.getFechaFin() != null) {
            stmt.setString(4, c.getFechaFin().toString());
        } else {
            stmt.setString(4, null);
        }

        stmt.setString(5, c.getSupervisoresCargo());
        stmt.setString(6, c.getDescripcionObjetivos());
    }
}
