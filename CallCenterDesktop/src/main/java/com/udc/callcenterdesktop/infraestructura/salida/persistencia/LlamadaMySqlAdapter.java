package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LlamadaMySqlAdapter implements ILlamadaRepository {
    
    // ... tus constantes SQL ...
    private static final String SELECT_ALL = "SELECT * FROM llamadas"; 

    @Override
    public void guardar(Llamada llamada) {
        // ... tu lógica de guardar ...
    }

    // CORRECCIÓN AQUÍ: El nombre debe coincidir con la interfaz
    public List<Llamada> listarTodos() { 
        List<Llamada> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = st.executeQuery()) {
             
            while (rs.next()) {
                // Mapeo simple para que compile
                Llamada l = new Llamada();
                l.setIdLlamada(rs.getLong("id_llamada"));
                // ... resto de setters ...
                lista.add(l);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // O lanza tu excepción personalizada
        }
        return lista;
    }

    @Override
    public List<LlamadaDTO> listarLlamadasConNombres() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Llamada> listarPorCliente(Long idCliente) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Llamada> listarTodas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}