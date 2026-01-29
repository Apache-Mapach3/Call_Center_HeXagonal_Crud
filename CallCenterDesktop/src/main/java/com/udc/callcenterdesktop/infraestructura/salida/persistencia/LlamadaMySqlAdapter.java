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
    
    private static final String INSERT = 
            "INSERT INTO llamadas (fecha_hora, duracion, detalle_resultado, observaciones, " +
            "id_agente, id_cliente, id_campania) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL = 
            "SELECT * FROM llamadas ORDER BY fecha_hora DESC";

    private static final String SELECT_WITH_NAMES = 
            "SELECT l.*, " +
            "a.nombre_completo AS nombre_agente, " +
            "c.nombre_completo AS nombre_cliente, " +
            "cam.nombre_campania AS nombre_campania " +
            "FROM llamadas l " +
            "INNER JOIN agentes a ON l.id_agente = a.id_agente " +
            "INNER JOIN clientes c ON l.id_cliente = c.id_cliente " +
            "INNER JOIN campanias cam ON l.id_campania = cam.id_campania " +
            "ORDER BY l.fecha_hora DESC";

    @Override
    public void guardar(Llamada llamada) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            st.setTimestamp(1, Timestamp.valueOf(llamada.getFechaHora()));
            st.setInt(2, llamada.getDuracion());
            st.setString(3, llamada.getDetalleResultado());
            st.setString(4, llamada.getObservaciones());
            st.setLong(5, llamada.getIdAgente());
            st.setLong(6, llamada.getIdCliente());
            st.setLong(7, llamada.getIdCampania());

            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    llamada.setIdLlamada(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al guardar llamada: " + e.getMessage());
        }
    }

    @Override
    public List<LlamadaDTO> listarLlamadasConNombres() {
        List<LlamadaDTO> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_WITH_NAMES);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                LlamadaDTO dto = new LlamadaDTO();
                dto.setIdLlamada(rs.getLong("id_llamada"));
                
                Timestamp ts = rs.getTimestamp("fecha_hora");
                if (ts != null) {
                    dto.setFechaHora(ts.toLocalDateTime());
                }
                
                dto.setDuracion(rs.getInt("duracion"));
                dto.setDetalleResultado(rs.getString("detalle_resultado"));
                dto.setObservaciones(rs.getString("observaciones"));
                dto.setIdAgente(rs.getLong("id_agente"));
                dto.setIdCliente(rs.getLong("id_cliente"));
                dto.setIdCampania(rs.getLong("id_campania"));
                
                // Nombres de las entidades relacionadas
                dto.setNombreAgente(rs.getString("nombre_agente"));
                dto.setNombreCliente(rs.getString("nombre_cliente"));
                dto.setNombreCampania(rs.getString("nombre_campania"));
                
                lista.add(dto);
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al listar llamadas: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Llamada> listarTodas() {
        List<Llamada> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                Llamada l = new Llamada();
                l.setIdLlamada(rs.getLong("id_llamada"));
                
                Timestamp ts = rs.getTimestamp("fecha_hora");
                if (ts != null) {
                    l.setFechaHora(ts.toLocalDateTime());
                }
                
                l.setDuracion(rs.getInt("duracion"));
                l.setDetalleResultado(rs.getString("detalle_resultado"));
                l.setObservaciones(rs.getString("observaciones"));
                l.setIdAgente(rs.getLong("id_agente"));
                l.setIdCliente(rs.getLong("id_cliente"));
                l.setIdCampania(rs.getLong("id_campania"));
                
                lista.add(l);
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al listar llamadas: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public List<Llamada> listarPorCliente(Long idCliente) {
        List<Llamada> lista = new ArrayList<>();
        String sql = "SELECT * FROM llamadas WHERE id_cliente = ? ORDER BY fecha_hora DESC";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setLong(1, idCliente);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Llamada l = new Llamada();
                    l.setIdLlamada(rs.getLong("id_llamada"));
                    
                    Timestamp ts = rs.getTimestamp("fecha_hora");
                    if (ts != null) {
                        l.setFechaHora(ts.toLocalDateTime());
                    }
                    
                    l.setDuracion(rs.getInt("duracion"));
                    l.setDetalleResultado(rs.getString("detalle_resultado"));
                    l.setObservaciones(rs.getString("observaciones"));
                    l.setIdAgente(rs.getLong("id_agente"));
                    l.setIdCliente(rs.getLong("id_cliente"));
                    l.setIdCampania(rs.getLong("id_campania"));
                    
                    lista.add(l);
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al listar llamadas por cliente: " + e.getMessage());
        }

        return lista;
    }
}