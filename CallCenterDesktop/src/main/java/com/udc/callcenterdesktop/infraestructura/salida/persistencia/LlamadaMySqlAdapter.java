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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador de persistencia para la entidad Llamada usando MySQL.
 * Implementa consultas optimizadas con JOINs para reducir queries N+1.
 * 
 * <p>Este adaptador tiene una particularidad: el método
 * {@code listarLlamadasConNombres()} retorna directamente DTOs
 * en lugar de entidades, porque incluye información de múltiples
 * tablas mediante JOINs SQL.</p>
 * 
 * <p><b>Optimizaciones implementadas:</b></p>
 * <ul>
 *   <li>JOINs para obtener nombres en una sola consulta</li>
 *   <li>Índices implícitos en claves foráneas</li>
 *   <li>Ordenamiento por fecha descendente</li>
 * </ul>
 * 
 * @author Carlos 
 * @version 2.0
 * @since 2025
 */
public class LlamadaMySqlAdapter implements ILlamadaRepository {

    //  CONSTANTES SQL
    
    private static final String SQL_INSERT = 
        "INSERT INTO llamadas (fecha_hora, duracion_segundos, detalle_resultado, " +
        "observaciones, id_agente, id_campania, id_cliente) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    /**
     * Consulta optimizada con JOINs para obtener nombres descriptivos.
     * Evita el problema N+1 al traer toda la información en una sola query.
     */
    private static final String SQL_SELECT_JOIN = 
        "SELECT " +
        "    l.id_llamada, " +
        "    l.fecha_hora, " +
        "    l.duracion_segundos, " +
        "    l.detalle_resultado, " +
        "    l.observaciones, " +
        "    l.id_agente, " +
        "    l.id_campania, " +
        "    l.id_cliente, " +
        "    a.nombre_completo AS nombre_agente, " +
        "    c.nombre_campania AS nombre_campania, " +
        "    cli.nombre_completo AS nombre_cliente " +
        "FROM llamadas l " +
        "INNER JOIN agentes a ON l.id_agente = a.id_agente " +
        "INNER JOIN campanias c ON l.id_campania = c.id_campania " +
        "INNER JOIN clientes cli ON l.id_cliente = cli.id_cliente " +
        "ORDER BY l.fecha_hora DESC";

    /**
     * Registra una nueva llamada en el sistema.
     * 
     * @param llamada entidad de dominio a guardar
     * @throws IllegalArgumentException si la llamada es null
     * @throws CallCenterException si ocurre un error de persistencia
     */
    @Override
    public void registrar(Llamada llamada) {
        validarLlamadaNoNula(llamada);
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatementRegistrar(stmt, llamada);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "No se pudo registrar la llamada en la base de datos"
                );
            }
            
            // Obtener el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    llamada.setIdLlamada(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            // Detectar violaciones de clave foránea
            if (e.getMessage().contains("foreign key constraint")) {
                throw new CallCenterException(
                    "Error de integridad referencial: Verifique que el agente, " +
                    "cliente y campaña existan en el sistema", e
                );
            }
            
            throw new CallCenterException(
                "Error crítico al registrar la llamada: " + e.getMessage(), e
            );
        }
    }

    /**
     * Recupera el historial completo de llamadas con información enriquecida.
     * 
     * <p>Este método retorna directamente DTOs (no entidades) porque
     * incluye información de múltiples tablas mediante JOINs. Esto
     * es una excepción al patrón Repository tradicional, justificada
     * por razones de performance.</p>
     * 
     * <p><b>Ventajas de este enfoque:</b></p>
     * <ul>
     *   <li>Una sola consulta en lugar de N+1 queries</li>
     *   <li>Reducción drástica de la latencia de red</li>
     *   <li>Menor carga en el servidor de BD</li>
     *   <li>Datos listos para mostrar en la UI</li>
     * </ul>
     * 
     * @return lista inmutable de DTOs con historial completo
     * @throws CallCenterException si ocurre un error de lectura
     */
    @Override
    public List<LlamadaDTO> listarLlamadasConNombres() {
        List<LlamadaDTO> llamadas = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_JOIN);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                llamadas.add(mapearLlamadaConNombres(rs));
            }
            
            return Collections.unmodifiableList(llamadas);
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al consultar el historial de llamadas: " + e.getMessage(), e
            );
        }
    }

    
    // MÉTODOS PRIVADOS DE UTILIDAD
    

    /**
     * Configura un PreparedStatement para el registro de llamadas.
     * Maneja la conversión de LocalDateTime a Timestamp de forma segura.
     * 
     * @param stmt statement a configurar
     * @param llamada datos origen
     * @throws SQLException si hay error al setear parámetros
     */
    private void configurarStatementRegistrar(PreparedStatement stmt, Llamada llamada) 
            throws SQLException {
        
        // Conversión de LocalDateTime a Timestamp
        if (llamada.getFechaHora() != null) {
            stmt.setTimestamp(1, Timestamp.valueOf(llamada.getFechaHora()));
        } else {
            stmt.setNull(1, Types.TIMESTAMP);
        }
        
        stmt.setInt(2, llamada.getDuracion());
        stmt.setString(3, llamada.getDetalleResultado());
        stmt.setString(4, llamada.getObservaciones());
        
        // IDs de relaciones (claves foráneas)
        stmt.setLong(5, llamada.getIdAgente());
        stmt.setLong(6, llamada.getIdCampania());
        stmt.setLong(7, llamada.getIdCliente());
    }

    /**
     * Mapea un ResultSet enriquecido (con JOINs) a un DTO completo.
     * 
     * <p>Este método lee tanto los campos de la tabla llamadas como
     * los nombres obtenidos mediante los JOINs.</p>
     * 
     * @param rs resultado de consulta SQL con JOINs
     * @return DTO completo con todos los nombres descriptivos
     * @throws SQLException si hay error al leer columnas
     */
    private LlamadaDTO mapearLlamadaConNombres(ResultSet rs) throws SQLException {
        LlamadaDTO dto = new LlamadaDTO();
        
        // Campos de la tabla llamadas
        dto.setIdLlamada(rs.getLong("id_llamada"));
        
        Timestamp timestamp = rs.getTimestamp("fecha_hora");
        if (timestamp != null) {
            dto.setFechaHora(timestamp.toLocalDateTime());
        }
        
        dto.setDuracion(rs.getInt("duracion_segundos"));
        dto.setDetalleResultado(rs.getString("detalle_resultado"));
        dto.setObservaciones(rs.getString("observaciones"));
        
        // IDs de relaciones
        dto.setIdAgente(rs.getLong("id_agente"));
        dto.setIdCampania(rs.getLong("id_campania"));
        dto.setIdCliente(rs.getLong("id_cliente"));
        
        // Nombres descriptivos obtenidos mediante JOINs
        dto.setNombreAgente(rs.getString("nombre_agente"));
        dto.setNombreCampania(rs.getString("nombre_campania"));
        dto.setNombreCliente(rs.getString("nombre_cliente"));
        
        return dto;
    }

    /**
     * Valida que la llamada no sea null.
     */
    private void validarLlamadaNoNula(Llamada llamada) {
        if (llamada == null) {
            throw new IllegalArgumentException("La llamada no puede ser null");
        }
    }
}