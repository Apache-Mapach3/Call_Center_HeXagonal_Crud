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
import java.util.Collections;
import java.util.List;

/**
 * Adaptador de persistencia para la entidad Campaña usando MySQL.
 * Implementa el patrón Repository con manejo especializado de fechas LocalDate.
 * 
 * <p>Este adaptador convierte correctamente entre java.time.LocalDate
 * y java.sql.Date, manejando nulls de forma segura.</p>
 * 
 * @author Carlos
 * @version 2.0
 * @since 2025
 */
public class CampaniaMySqlAdapter implements ICampaniaRepository {

    // CONSTANTES SQL
    
    private static final String SQL_INSERT = 
        "INSERT INTO campanias (nombre_campania, tipo_campania, fecha_inicio, " +
        "fecha_fin, supervisores_cargo, descripcion_objetivos) " +
        "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT id_campania, nombre_campania, tipo_campania, fecha_inicio, " +
        "fecha_fin, supervisores_cargo, descripcion_objetivos " +
        "FROM campanias " +
        "ORDER BY fecha_inicio DESC";
    
    private static final String SQL_UPDATE = 
        "UPDATE campanias SET " +
        "nombre_campania = ?, " +
        "tipo_campania = ?, " +
        "fecha_inicio = ?, " +
        "fecha_fin = ?, " +
        "supervisores_cargo = ?, " +
        "descripcion_objetivos = ? " +
        "WHERE id_campania = ?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM campanias WHERE id_campania = ?";

    /**
     * Persiste una nueva campaña en la base de datos.
     * 
     * @param campania entidad de dominio a guardar
     * @throws IllegalArgumentException si la campaña es null
     * @throws CallCenterException si ocurre un error de persistencia
     */
    @Override
    public void guardar(Campania campania) {
        validarCampaniaNoNula(campania);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatementGuardar(stmt, campania);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "No se pudo guardar la campaña en la base de datos"
                );
            }
            
            // Obtener el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    campania.setIdCampania(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al guardar campaña: " + e.getMessage(), e
            );
        }
    }

    /**
     * Recupera todas las campañas registradas.
     * 
     * @return lista inmutable de campañas ordenadas por fecha de inicio
     * @throws CallCenterException si ocurre un error de lectura
     */
    @Override
    public List<Campania> listarTodos() {
        List<Campania> campanias = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                campanias.add(mapearCampania(rs));
            }
            
            return Collections.unmodifiableList(campanias);
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al consultar la lista de campañas: " + e.getMessage(), e
            );
        }
    }

    /**
     * Actualiza los datos de una campaña existente.
     * 
     * @param campania entidad con datos actualizados
     * @throws IllegalArgumentException si la campaña es null o no tiene ID
     * @throws CallCenterException si la campaña no existe o hay error de BD
     */
    @Override
    public void actualizar(Campania campania) {
        validarCampaniaNoNula(campania);
        validarIdCampania(campania.getIdCampania());
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            
            configurarStatementActualizar(stmt, campania);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "Campaña con ID " + campania.getIdCampania() + " no encontrada"
                );
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al actualizar campaña: " + e.getMessage(), e
            );
        }
    }

    /**
     * Elimina una campaña del sistema de forma permanente.
     * 
     * @param id identificador único de la campaña
     * @throws IllegalArgumentException si el ID es null o inválido
     * @throws CallCenterException si la campaña no existe o hay error de BD
     */
    @Override
    public void eliminar(Long id) {
        validarIdCampania(id);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            
            stmt.setLong(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "Campaña con ID " + id + " no encontrada"
                );
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al eliminar campaña: " + e.getMessage(), e
            );
        }
    }

   
    // MÉTODOS PRIVADOS DE UTILIDAD
    
    /**
     * Configura un PreparedStatement para operaciones de inserción.
     * Maneja la conversión segura de LocalDate a java.sql.Date.
     * 
     * @param stmt statement a configurar
     * @param campania datos origen
     * @throws SQLException si hay error al setear parámetros
     */
    private void configurarStatementGuardar(PreparedStatement stmt, Campania campania) 
            throws SQLException {
        stmt.setString(1, campania.getNombreCampania());
        stmt.setString(2, campania.getTipoCampania());
        
        // Manejo seguro de fecha de inicio
        if (campania.getFechaInicio() != null) {
            stmt.setDate(3, Date.valueOf(campania.getFechaInicio()));
        } else {
            stmt.setNull(3, Types.DATE);
        }
        
        // Manejo seguro de fecha de fin (puede ser null para campañas indefinidas)
        if (campania.getFechaFin() != null) {
            stmt.setDate(4, Date.valueOf(campania.getFechaFin()));
        } else {
            stmt.setNull(4, Types.DATE);
        }
        
        stmt.setString(5, campania.getSupervisoresCargo());
        stmt.setString(6, campania.getDescripcionObjetivos());
    }

    /**
     * Configura un PreparedStatement para operaciones de actualización.
     * 
     * @param stmt statement a configurar
     * @param campania datos origen
     * @throws SQLException si hay error al setear parámetros
     */
    private void configurarStatementActualizar(PreparedStatement stmt, Campania campania) 
            throws SQLException {
        configurarStatementGuardar(stmt, campania);
        stmt.setLong(7, campania.getIdCampania());
    }

    /**
     * Mapea un ResultSet a una entidad Campaña.
     * Convierte java.sql.Date a java.time.LocalDate de forma segura.
     * 
     * @param rs resultado de consulta SQL
     * @return entidad Campaña poblada
     * @throws SQLException si hay error al leer columnas
     */
    private Campania mapearCampania(ResultSet rs) throws SQLException {
        return new Campania(
            rs.getLong("id_campania"),
            rs.getString("nombre_campania"),
            rs.getString("tipo_campania"),
            convertirFecha(rs.getDate("fecha_inicio")),
            convertirFecha(rs.getDate("fecha_fin")),
            rs.getString("supervisores_cargo"),
            rs.getString("descripcion_objetivos")
        );
    }

    /**
     * Convierte java.sql.Date a java.time.LocalDate de forma segura.
     * Retorna null si la fecha SQL es null.
     * 
     * @param fechaSql fecha en formato SQL
     * @return fecha en formato LocalDate, o null si la entrada es null
     */
    private LocalDate convertirFecha(Date fechaSql) {
        return fechaSql != null ? fechaSql.toLocalDate() : null;
    }

    /**
     * Valida que la campaña no sea null.
     */
    private void validarCampaniaNoNula(Campania campania) {
        if (campania == null) {
            throw new IllegalArgumentException("La campaña no puede ser null");
        }
    }

    /**
     * Valida que el ID de la campaña sea válido.
     */
    private void validarIdCampania(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de campaña inválido: " + id);
        }
    }
}