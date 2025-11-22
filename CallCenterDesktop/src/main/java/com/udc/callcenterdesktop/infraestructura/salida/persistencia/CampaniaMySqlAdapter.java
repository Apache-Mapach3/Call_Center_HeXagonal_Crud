/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;


import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;


import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;
import java.util.List;

/**
 *
 * @author camolano
 */
public class CampaniaMySqlAdapter extends ICampaniaRepository {
    private static final String SQL_INSERT = 
        "INSERT INTO campanias (nombre, descripcion, fechaInicio, fechaFin, estado) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE = 
        "UPDATE campanias SET nombre=?, descripcion=?, fechaInicio=?, fechaFin=?, estado=? WHERE id=?";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT id, nombre, descripcion, fechaInicio, fechaFin, estado FROM campanias";
    
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id, nombre, descripcion, fechaInicio, fechaFin, estado FROM campanias WHERE id = ?";
    
    private static final String SQL_DELETE_BY_ID = 
        "DELETE FROM campanias WHERE id = ?";


private Campania mapearCampania(ResultSet rs) throws SQLException {
        Campania campania = new Campania();
        campania.setId(rs.getInt("id"));
        campania.setNombre(rs.getString("nombre"));
        campania.setDescripcion(rs.getString("descripcion"));
        
        campania.setFechaInicio(rs.getDate("fechaInicio").toLocalDate()); 
        campania.setFechaFin(rs.getDate("fechaFin").toLocalDate());
        campania.setEstado(rs.getString("estado"));
        return campania;
    }

public Campania guardar(Campania campania) {
       
        if (campania.getId() == 0) {
            return insertar(campania);
        } else {
            return actualizar(campania);
        }
    }


private Campania insertar(Campania campania) {

try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {



stmt.setString(1, campania.getNombre());
            stmt.setString(2, campania.getDescripcion());

stmt.setDate(3, Date.valueOf(campania.getFechaInicio())); 
            stmt.setDate(4, Date.valueOf(campania.getFechaFin()));
            stmt.setString(5, campania.getEstado());


int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Fallo al insertar campaña, no se afectaron filas.");
            }

try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    campania.setId(generatedKeys.getInt(1));


}
            }
            return campania;




} catch (SQLException e) {



throw new RuntimeException("Error al crear campaña en BD.", e);
        }
    }

private Campania actualizar(Campania campania) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

stmt.setString(1, campania.getNombre());
            stmt.setString(2, campania.getDescripcion());
            stmt.setDate(3, Date.valueOf(campania.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(campania.getFechaFin()));
            stmt.setString(5, campania.getEstado());
            stmt.setInt(6, campania.getId());



stmt.executeUpdate();
            
            return campania;



} catch (SQLException e) {
            System.err.println("Error de persistencia al actualizar la campaña: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar campaña en BD.", e);
        }
    }

    public Optional<Campania> buscarPorId(int id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si encuentra un registro, lo mapea y envuelve en Optional
                    return Optional.of(mapearCampania(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de persistencia al buscar por ID: " + e.getMessage());
            e.printStackTrace();

}
        return Optional.empty();
    }

    public List<Campania> buscarTodas() {
        List<Campania> campanias = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                campanias.add(mapearCampania(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error de persistencia al buscar todas: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener listado de campañas.", e);
        }
        return campanias;
    }

    public boolean eliminarPorId(int id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_BY_ID)) {
            
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();

return filasAfectadas > 0; 
        } catch (SQLException e) {
            System.err.println("Error de persistencia al eliminar: " + e.getMessage());
            e.printStackTrace();
            return false; 
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
