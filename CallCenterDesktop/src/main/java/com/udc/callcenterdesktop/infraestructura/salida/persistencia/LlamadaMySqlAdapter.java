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
import java.util.List;

public class LlamadaMySqlAdapter implements ILlamadaRepository {

    private static final String INSERT_SQL = "INSERT INTO llamadas (fecha_hora, duracion_segundos, detalle_resultado, observaciones, id_agente, id_campania, id_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_JOIN_SQL = 
            "SELECT l.id_llamada, l.fecha_hora, l.duracion_segundos, l.detalle_resultado, l.observaciones, " +
            "a.nombre_completo AS nom_agente, " +
            "c.nombre_campania AS nom_campania, " +
            "cli.nombre_completo AS nom_cliente " +
            "FROM llamadas l " +
            "INNER JOIN agentes a ON l.id_agente = a.id_agente " +
            "INNER JOIN campanias c ON l.id_campania = c.id_campania " +
            "INNER JOIN clientes cli ON l.id_cliente = cli.id_cliente " +
            "ORDER BY l.fecha_hora DESC";

    @Override
    public void registrar(Llamada llamada) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(llamada.getFechaHora()));
            stmt.setInt(2, llamada.getDuracion());
            stmt.setString(3, llamada.getDetalleResultado());
            stmt.setString(4, llamada.getObservaciones());
            stmt.setLong(5, llamada.getIdAgente());
            stmt.setLong(6, llamada.getIdCampania());
            stmt.setLong(7, llamada.getIdCliente());
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new CallCenterException("Error crítico al registrar la llamada en BD.", e);
        }
    }

    // ESTE ES EL MÉTODO QUE DABA ERROR DE OVERRIDE
    @Override
    public List<LlamadaDTO> listarLlamadasConNombres() {
        List<LlamadaDTO> lista = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOIN_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LlamadaDTO dto = new LlamadaDTO();
                dto.idLlamada = rs.getLong("id_llamada");
                dto.fechaHora = rs.getTimestamp("fecha_hora").toLocalDateTime();
                dto.duracion = rs.getInt("duracion_segundos");
                dto.detalleResultado = rs.getString("detalle_resultado");
                dto.observaciones = rs.getString("observaciones");
                
                // Mapeo de nombres (JOIN)
                dto.nombreAgente = rs.getString("nom_agente");
                dto.nombreCampania = rs.getString("nom_campania");
                dto.nombreCliente = rs.getString("nom_cliente");
                
                lista.add(dto);
            }
            return lista;
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al consultar el historial.", e);
        }
    }
}