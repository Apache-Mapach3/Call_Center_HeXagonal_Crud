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


public class LlamadaMySqlAdapter implements ILlamadaRepository {

    private static final String INSERT_SQL = 
            "INSERT INTO llamadas (fecha_hora, duracion_segundos, detalle_resultado, observaciones, id_agente, id_cliente, id_campania) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
   
    private static final String SELECT_HISTORY_SQL = 
            "SELECT l.id_llamada, l.fecha_hora, l.detalle_resultado, " +
            "a.nombre_completo AS nom_agente, " +
            "c.nombre_completo AS nom_cliente, " +
            "camp.nombre_campania AS nom_campania " +
            "FROM llamadas l " +
            "INNER JOIN agentes a ON l.id_agente = a.id_agente " +
            "INNER JOIN clientes c ON l.id_cliente = c.id_cliente " +
            "INNER JOIN campanias camp ON l.id_campania = camp.id_campania " +
            "ORDER BY l.id_llamada DESC LIMIT 50";

   
    public void registrar(LlamadaDTO llamada) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
           
            stmt.setString(1, llamada.getFechaHora().toString());
            
            stmt.setInt(2, llamada.getDuracion());
            stmt.setString(3, llamada.getDetalleResultado());
            stmt.setString(4, llamada.getObservaciones());
            stmt.setLong(5, llamada.getIdAgente());
            stmt.setLong(6, llamada.getIdCliente());
            stmt.setLong(7, llamada.getIdCampania());
            
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new CallCenterException("No se guardó la llamada.");

        } catch (SQLException e) {
            throw new CallCenterException("Error BD al registrar llamada", e);
        }
    }

    @Override
    public List<LlamadaDTO> listarLlamadasConNombres() {
        List<LlamadaDTO> historial = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_HISTORY_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LlamadaDTO dto = new LlamadaDTO();
                dto.setIdLlamada(rs.getLong("id_llamada"));
                
               
                String fechaStr = rs.getString("fecha_hora");
                if (fechaStr != null && !fechaStr.isEmpty()) {
                    dto.setFechaHora(LocalDateTime.parse(fechaStr));
                }
                
                dto.setDetalleResultado(rs.getString("detalle_resultado"));
                
                
                dto.setNombreAgente(rs.getString("nom_agente"));
                dto.setNombreCliente(rs.getString("nom_cliente"));
                dto.setNombreCampania(rs.getString("nom_campania"));
                
                historial.add(dto);
            }
        } catch (SQLException e) {
            throw new CallCenterException("Error BD al obtener historial", e);
        }
        return historial;
    }

    @Override
    public void registrar(Llamada llamada) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
