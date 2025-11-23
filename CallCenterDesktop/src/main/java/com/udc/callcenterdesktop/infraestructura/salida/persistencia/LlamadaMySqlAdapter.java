/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LlamadaMySqlAdapter implements ILlamadaRepository {

    
    private final ConexionDB conexionDB = new ConexionDB(); 
    
    
    private static final String TABLA_LLAMADAS = "llamadas"; 
    
    private static final String ID_LLAMADA = "id_llamada";
    private static final String FECHA_HORA = "fecha_hora";
    private static final String DURACION = "duracion";
    private static final String DETALLE = "detalle";
    private static final String OBSERVACION = "observacion";
    private static final String ID_AGENTE = "id_agente";
    private static final String ID_CAMPANIA = "id_campania";
    private static final String ID_CLIENTE = "id_cliente";

    
    private Llamada mapearResultSetALlamada(ResultSet rs) throws SQLException {
        Llamada llamada = new Llamada();
        llamada.setIdLlamada(rs.getInt(ID_LLAMADA));
        llamada.setFechaHora(rs.getTimestamp(FECHA_HORA).toLocalDateTime());
        llamada.setDuracion(rs.getInt(DURACION));
        llamada.setDetalle(rs.getString(DETALLE));
        llamada.setObservacion(rs.getString(OBSERVACION));
        
       
        llamada.setIdAgente(rs.getInt(ID_AGENTE));
        llamada.setIdCampania(rs.getInt(ID_CAMPANIA));
        llamada.setIdCliente(rs.getInt(ID_CLIENTE));
        
        return llamada;
    }

   

  

    @Override
    public Llamada guardar(Llamada llamada) {
       
        String sql;
        boolean esNuevo = llamada.getIdLlamada() == 0;

        if (esNuevo) {
         
            sql = "INSERT INTO " + TABLA_LLAMADAS + 
                  " (" + FECHA_HORA + ", " + DURACION + ", " + DETALLE + ", " + OBSERVACION + 
                  ", " + ID_AGENTE + ", " + ID_CAMPANIA + ", " + ID_CLIENTE + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
          
            sql = "UPDATE " + TABLA_LLAMADAS + " SET " +
                  DURACION + " = ?, " + DETALLE + " = ?, " + OBSERVACION + " = ?, " +
                  ID_AGENTE + " = ?, " + ID_CAMPANIA + " = ?, " + ID_CLIENTE + " = ? WHERE " + ID_LLAMADA + " = ?";
        }

        try (Connection conn = conexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql, esNuevo ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {
            
            int index = 1;
            
            if (esNuevo) {
              
                pstmt.setTimestamp(index++, Timestamp.valueOf(LocalDateTime.now())); 
                pstmt.setInt(index++, llamada.getDuracion());
                pstmt.setString(index++, llamada.getDetalle());
                pstmt.setString(index++, llamada.getObservacion());
                pstmt.setInt(index++, llamada.getIdAgente());
                pstmt.setInt(index++, llamada.getIdCampania());
                pstmt.setInt(index++, llamada.getIdCliente());

            } else {
              
                pstmt.setInt(index++, llamada.getDuracion());
                pstmt.setString(index++, llamada.getDetalle());
                pstmt.setString(index++, llamada.getObservacion());
                pstmt.setInt(index++, llamada.getIdAgente());
                pstmt.setInt(index++, llamada.getIdCampania());
                pstmt.setInt(index++, llamada.getIdCliente());
             
                pstmt.setInt(index++, llamada.getIdLlamada()); 
            }

            int filasAfectadas = pstmt.executeUpdate();

            if (esNuevo && filasAfectadas > 0) {

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        llamada.setIdLlamada(rs.getInt(1));
                    }
                }
            }
            return llamada;

        } catch (SQLException e) {
            throw new CallCenterException("Error al guardar la llamada en la base de datos: " + e.getMessage(), e);
        }
    }

  
    public Optional<Llamada> buscarPorId(int id) {
      
        String sql = "SELECT * FROM " + TABLA_LLAMADAS + " WHERE " + ID_LLAMADA + " = ?";
        
        try (Connection conn = conexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultSetALlamada(rs));
                }
            }
        } catch (SQLException e) {
            throw new CallCenterException("Error al buscar llamada por ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

  
    public List<Llamada> buscarTodas() {
        List<Llamada> llamadas = new ArrayList<>();
        
        
        String sql = "SELECT L.* "
                + "FROM " + TABLA_LLAMADAS + " L "
                // El join lo realizamos por si el Servicio decide cambiar el mapeo
                // para obtener nombres directamente del Adaptador (práctica menos Hexagonal)
                + "INNER JOIN Agente A ON L." + ID_AGENTE + " = A.id_agente "
                + "INNER JOIN Campanias C ON L." + ID_CAMPANIA + " = C.id_campania "
                + "INNER JOIN Cliente CL ON L." + ID_CLIENTE + " = CL.id_cliente";
        
        try (Connection conn = conexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                llamadas.add(mapearResultSetALlamada(rs));
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al obtener todas las llamadas: " + e.getMessage(), e);
        }
        return llamadas;
    }

  
    public boolean eliminarPorId(int id) {
        String sql = "DELETE FROM " + TABLA_LLAMADAS + " WHERE " + ID_LLAMADA + " = ?";

        try (Connection conn = conexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
        
            throw new CallCenterException("Error al eliminar la llamada: " + e.getMessage(), e);
        }
    }
}