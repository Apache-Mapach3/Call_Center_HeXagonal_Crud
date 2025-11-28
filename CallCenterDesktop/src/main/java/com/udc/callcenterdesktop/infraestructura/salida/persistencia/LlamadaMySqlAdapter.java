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
 * Adaptador de persistencia para la entidad Llamada (SQLite).
 * Implementa el patrón Repository para abstraer el acceso a datos.
 * 
 * <p>Esta clase maneja la persistencia de llamadas en SQLite,
 * incluyendo consultas con JOINs para obtener nombres de entidades relacionadas.</p>
 * 
 * @author Carlos
 * @version 3.0 - SQLite Edition
 * @since 2025
 */
public class LlamadaMySqlAdapter implements ILlamadaRepository {

    // SQL para insertar una nueva llamada
    private static final String INSERT_SQL = 
            "INSERT INTO llamadas (fecha_hora, duracion_segundos, detalle_resultado, observaciones, id_agente, id_cliente, id_campania) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    // SQL para obtener historial con JOINs (nombres incluidos)
    private static final String SELECT_HISTORY_SQL = 
            "SELECT l.id_llamada, l.fecha_hora, l.duracion_segundos, l.detalle_resultado, l.observaciones, " +
            "a.nombre_completo AS nom_agente, " +
            "c.nombre_completo AS nom_cliente, " +
            "camp.nombre_campania AS nom_campania " +
            "FROM llamadas l " +
            "INNER JOIN agentes a ON l.id_agente = a.id_agente " +
            "INNER JOIN clientes c ON l.id_cliente = c.id_cliente " +
            "INNER JOIN campanias camp ON l.id_campania = camp.id_campania " +
            "ORDER BY l.id_llamada DESC LIMIT 50";

    /**
     * Registra una nueva llamada en la base de datos.
     * 
     * <p><b>IMPORTANTE:</b> Este método recibe una entidad de dominio {@link Llamada},
     * NO un DTO. La conversión de DTO a Entidad se hace en la capa de servicio.</p>
     * 
     * @param llamada entidad de dominio a persistir
     * @throws CallCenterException si ocurre un error de persistencia
     */
    @Override
public void registrar(Llamada llamada) { // ← RECIBE ENTIDAD, NO DTO
    try (Connection conn = ConexionDB.obtenerConexion();
         PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
        
        configurarStatementInsertar(stmt, llamada);
        stmt.executeUpdate();
        
    } catch (SQLException e) {
        throw new CallCenterException("Error al guardar", e);
    }
}

    /**
     * Lista el historial de llamadas con nombres de entidades relacionadas.
     * 
     * <p>Esta consulta utiliza JOINs para obtener los nombres de:</p>
     * <ul>
     *   <li>Agente que atendió</li>
     *   <li>Cliente contactado</li>
     *   <li>Campaña asociada</li>
     * </ul>
     * 
     * <p>Esto evita el problema N+1 y mejora el rendimiento.</p>
     * 
     * @return lista de DTOs con información completa de las llamadas
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
    
   
    // MÉTODOS PRIVADOS DE UTILIDAD
    
    /**
     * Configura los parámetros del PreparedStatement para insertar.
     * 
     * @param stmt PreparedStatement a configurar
     * @param llamada entidad con los datos a insertar
     * @throws SQLException si ocurre un error al configurar parámetros
     */
    private void configurarStatementInsertar(PreparedStatement stmt, Llamada llamada) throws SQLException {
        // Parámetro 1: fecha_hora (como String ISO-8601)
        if (llamada.getFechaHora() != null) {
            stmt.setString(1, llamada.getFechaHora().toString());
        } else {
            stmt.setString(1, LocalDateTime.now().toString());
        }
        
        // Parámetro 2: duracion_segundos
        stmt.setInt(2, llamada.getDuracion());
        
        // Parámetro 3: detalle_resultado
        stmt.setString(3, llamada.getDetalleResultado());
        
        // Parámetro 4: observaciones (puede ser null)
        stmt.setString(4, llamada.getObservaciones());
        
        // Parámetro 5: id_agente
        stmt.setLong(5, llamada.getIdAgente());
        
        // Parámetro 6: id_cliente
        stmt.setLong(6, llamada.getIdCliente());
        
        // Parámetro 7: id_campania
        stmt.setLong(7, llamada.getIdCampania());
    }
    
    /**
     * Mapea un ResultSet (con JOINs) a un DTO de Llamada.
     * 
     * <p>Este método maneja la conversión de los datos obtenidos
     * de la consulta SQL que incluye JOINs con las tablas relacionadas.</p>
     * 
     * @param rs ResultSet con los datos de la consulta
     * @return DTO mapeado con toda la información
     * @throws SQLException si ocurre un error al leer el ResultSet
     */
    private LlamadaDTO mapearLlamadaConNombres(ResultSet rs) throws SQLException {
        LlamadaDTO dto = new LlamadaDTO();
        
        // Mapear campos básicos de la llamada
        dto.setIdLlamada(rs.getLong("id_llamada"));
        dto.setDuracion(rs.getInt("duracion_segundos"));
        dto.setDetalleResultado(rs.getString("detalle_resultado"));
        dto.setObservaciones(rs.getString("observaciones"));
        
        // Mapear fecha/hora (convertir de String a LocalDateTime)
        String fechaStr = rs.getString("fecha_hora");
        if (fechaStr != null && !fechaStr.isEmpty()) {
            try {
                dto.setFechaHora(LocalDateTime.parse(fechaStr));
            } catch (Exception e) {
                // Si falla el parseo, usar fecha actual
                dto.setFechaHora(LocalDateTime.now());
            }
        }
        
        // Mapear nombres obtenidos de los JOINs
        dto.setNombreAgente(rs.getString("nom_agente"));
        dto.setNombreCliente(rs.getString("nom_cliente"));
        dto.setNombreCampania(rs.getString("nom_campania"));
        
        return dto;
    }
}