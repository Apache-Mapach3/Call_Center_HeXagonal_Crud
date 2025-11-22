/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;


import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de Persistencia (Capa de Infraestructura).
 * Implementa el contrato ICampaniaRepository usando JDBC para interactuar con MySQL.
 */
public class CampaniaMySqlAdapter implements ICampaniaRepository {


    private static final String SQL_INSERT = 
        "INSERT INTO campanias (nombre_campania, tipo_campania, fecha_inicio, fecha_fin, supervisores_cargo, descripcion_objetivos) VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE = 
        "UPDATE campanias SET nombre_campania=?, tipo_campania=?, fecha_inicio=?, fecha_fin=?, supervisores_cargo=?, descripcion_objetivos=? WHERE id_campania=?";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT id_campania, nombre_campania, tipo_campania, fecha_inicio, fecha_fin, supervisores_cargo, descripcion_objetivos FROM campanias";
    
    private static final String SQL_SELECT_BY_ID = 
        "SELECT id_campania, nombre_campania, tipo_campania, fecha_inicio, fecha_fin, supervisores_cargo, descripcion_objetivos FROM campanias WHERE id_campania=?";
        
    private static final String SQL_DELETE = 
        "DELETE FROM campanias WHERE id_campania=?";

    // ------------------------------------------
    // 2. IMPLEMENTACIÓN: Mapeo de ResultSet a Entidad
    // ------------------------------------------
    
    /**
     * Helper para mapear una fila de ResultSet a un objeto Campania.
     * @param rs El ResultSet actual.
     * @return Objeto Campania mapeado.
     * @throws SQLException Si ocurre un error de JDBC.
     */
    private Campania mapearCampania(ResultSet rs) throws SQLException {
        Campania campania = new Campania();
        campania.setId(rs.getInt("id_campania"));
        campania.setNombre(rs.getString("nombre_campania"));
        campania.setTipoCampania(rs.getString("tipo_campania"));
        
        // Mapeo de fechas (DATE a LocalDate)
        Date fechaInicioSql = rs.getDate("fecha_inicio");
        campania.setFechaInicio(fechaInicioSql != null ? fechaInicioSql.toLocalDate() : null);
        
        Date fechaFinSql = rs.getDate("fecha_fin");
        campania.setFechaFin(fechaFinSql != null ? fechaFinSql.toLocalDate() : null);
        
        campania.setSupervisoresCargo(rs.getString("supervisores_cargo"));
        campania.setDescripcionObjetivos(rs.getString("descripcion_objetivos"));
        // Nota: Se asume que el estado es manejado por lógica o por defecto 'ACTIVA'
        campania.setEstado("ACTIVA"); 
        
        return campania;
    }

    
    
    public Campania guardar(Campania campania) {
        // Decide si es INSERT o UPDATE basado en si el ID existe (ID > 0)
        boolean isUpdate = campania.getId() > 0;
        String sql = isUpdate ? SQL_UPDATE : SQL_INSERT;
        
        try (Connection conn = ConexionDB.obtenerConexion();
             // RETURN_GENERATED_KEYS es necesario para obtener el ID si es un INSERT
             PreparedStatement stmt = conn.prepareStatement(sql, isUpdate ? Statement.NO_GENERATED_KEYS : Statement.RETURN_GENERATED_KEYS)) {

            // Mapeo de parámetros del objeto Campania al PreparedStatement
            stmt.setString(1, campania.getNombre());
            stmt.setString(2, campania.getTipoCampania());
            stmt.setDate(3, Date.valueOf(campania.getFechaInicio()));
            
            // Las fechas fin pueden ser NULL, se maneja la conversión de LocalDate a SQL Date
            LocalDate fechaFin = campania.getFechaFin();
            if (fechaFin != null) {
                stmt.setDate(4, Date.valueOf(fechaFin));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            
            stmt.setString(5, campania.getSupervisoresCargo());
            stmt.setString(6, campania.getDescripcionObjetivos());

            if (isUpdate) {
                // Si es UPDATE, el ID va al final (posición 7)
                stmt.setInt(7, campania.getId());
            }

            stmt.executeUpdate();

            // Si es INSERT, recuperamos el ID generado
            if (!isUpdate) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        campania.setId(rs.getInt(1));
                    }
                }
            }
            return campania;

        } catch (SQLException e) {
            // Se lanza una excepción de tiempo de ejecución para ser manejada en capas superiores
            throw new RuntimeException("Error al guardar/actualizar la campaña en la base de datos.", e);
        }
    }

   
    public Optional<Campania> buscarPorId(int id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Si encuentra el registro, mapea y devuelve un Optional con la Campania
                    return Optional.of(mapearCampania(rs));
                }
            }
            // Si no encuentra nada, devuelve un Optional vacío
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar campaña por ID.", e);
        }
    }


    public List<Campania> buscarTodas() {
        List<Campania> campanias = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) { // El ResultSet se cierra con el Statement y Connection gracias al try-with-resources

            while (rs.next()) {
                // Itera sobre todos los resultados y mapea cada fila
                campanias.add(mapearCampania(rs));
            }
            return campanias;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar todas las campañas.", e);
        }
    }

   
    public boolean eliminarPorId(int id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setInt(1, id);
            
            // executeUpdate devuelve el número de filas afectadas
            int filasAfectadas = stmt.executeUpdate(); 
            
            // Retorna true si al menos una fila fue eliminada
            return filasAfectadas > 0; 

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la campaña.", e);
        }
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

