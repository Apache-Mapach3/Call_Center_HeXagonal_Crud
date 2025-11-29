/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de persistencia para Llamadas en MySQL Server.
 * 
 * <p><b>Optimizaciones MySQL:</b></p>
 * <ul>
 *   <li>Uso de tipo DATETIME nativo de MySQL</li>
 *   <li>Conversión automática LocalDateTime ↔ java.sql.Timestamp</li>
 *   <li>JOIN optimizado con índices</li>
 * </ul>
 * 
 * @author Carlos
 * @version 4.0 - MySQL Edition
 */
public class LlamadaMySqlAdapter implements ILlamadaRepository {

    // SQL para insertar una nueva llamada
    private static final String INSERT_SQL = 
            "INSERT INTO llamadas (fecha_hora, duracion_segundos, detalle_resultado, " +
            "observaciones, id_agente, id_cliente, id_campania) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    // SQL para obtener historial con JOINs (nombres incluidos)
    private static final String SELECT_HISTORY_SQL = 
            "SELECT " +
            "l.id_llamada, l.fecha_hora, l.duracion_segundos, l.detalle_resultado, l.observaciones, " +
            "a.nombre_completo AS nom_agente, " +
            "c.nombre_completo AS nom_cliente, " +
            "camp.nombre_campania AS nom_campania " +
            "FROM llamadas l " +
            "INNER JOIN agentes a ON l.id_agente = a.id_agente " +
            "INNER JOIN clientes c ON l.id_cliente = c.id_cliente " +
            "INNER JOIN campanias camp ON l.id_campania = camp.id_campania " +
            "ORDER BY l.fecha_hora DESC LIMIT 100";

    /**
     * Registra una nueva llamada en la base de datos.
     * 
     * @param llamada entidad de dominio a persistir
     * @throws CallCenterException si ocurre un error de persistencia
     */
    @Override
    public void registrar(Llamada llamada) {
        if (llamada == null) {
            throw new IllegalArgumentException("La entidad Llamada no puede ser null");
        }
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatementLlamada(stmt, llamada);
            
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new CallCenterException(
                    "No se pudo registrar la llamada, no se afectaron filas."
                );
            }
            
            // Recuperar el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    llamada.setIdLlamada(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            // Manejo específico de errores de Foreign Key
            if (e.getErrorCode() == 1452) { // Cannot add or update a child row
                throw new CallCenterException(
                    "Error de integridad referencial: Verifique que el Agente, " +
                    "Cliente y Campaña existan en la base de datos.",
                    e
                );
            }
            throw new CallCenterException("Error de BD al registrar llamada", e);
        }
    }

    /**
     * Lista el historial de llamadas con nombres de entidades relacionadas.
     * 
     * @return lista de DTOs con información completa
     */
    @Override
    public List<LlamadaDTO> listarLlamadasConNombres() {
        List<LlamadaDTO> historial = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_HISTORY_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LlamadaDTO dto = mapearLlamadaConNombres(rs);
                historial.add(dto);
            }
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al obtener historial de llamadas", e);
        }
        
        return historial;
    }
    
    /**
     * Configura los parámetros del PreparedStatement.
     * IMPORTANTE: Conversión correcta de LocalDateTime a java.sql.Timestamp para MySQL.
     */
    private void configurarStatementLlamada(PreparedStatement stmt, Llamada llamada) 
            throws SQLException {
        
        // Parámetro 1: fecha_hora (DATETIME en MySQL)
        // Conversión: LocalDateTime → java.sql.Timestamp
        if (llamada.getFechaHora() != null) {
            stmt.setTimestamp(1, Timestamp.valueOf(llamada.getFechaHora()));
        } else {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
        }
        
        // Parámetro 2: duracion_segundos (INT en MySQL)
        stmt.setInt(2, llamada.getDuracion());
        
        // Parámetro 3: detalle_resultado (VARCHAR en MySQL)
        stmt.setString(3, llamada.getDetalleResultado());
        
        // Parámetro 4: observaciones (TEXT en MySQL, puede ser NULL)
        if (llamada.getObservaciones() != null && !llamada.getObservaciones().trim().isEmpty()) {
            stmt.setString(4, llamada.getObservaciones());
        } else {
            stmt.setNull(4, Types.VARCHAR);
        }
        
        // Parámetros 5-7: Foreign Keys
        stmt.setLong(5, llamada.getIdAgente());
        stmt.setLong(6, llamada.getIdCliente());
        stmt.setLong(7, llamada.getIdCampania());
    }
    
    /**
     * Mapea un ResultSet (con JOINs) a un DTO de Llamada.
     * IMPORTANTE: Conversión correcta de java.sql.Timestamp a LocalDateTime.
     */
    private LlamadaDTO mapearLlamadaConNombres(ResultSet rs) throws SQLException {
        LlamadaDTO dto = new LlamadaDTO();
        
        // Campos básicos de la llamada
        dto.setIdLlamada(rs.getLong("id_llamada"));
        dto.setDuracion(rs.getInt("duracion_segundos"));
        dto.setDetalleResultado(rs.getString("detalle_resultado"));
        dto.setObservaciones(rs.getString("observaciones"));
        
        // Fecha/hora: Conversión java.sql.Timestamp → LocalDateTime
        Timestamp fechaHoraTimestamp = rs.getTimestamp("fecha_hora");
        if (fechaHoraTimestamp != null) {
            dto.setFechaHora(fechaHoraTimestamp.toLocalDateTime());
        }
        
        // Nombres obtenidos de los JOINs
        dto.setNombreAgente(rs.getString("nom_agente"));
        dto.setNombreCliente(rs.getString("nom_cliente"));
        dto.setNombreCampania(rs.getString("nom_campania"));
        
        return dto;
    }
}
