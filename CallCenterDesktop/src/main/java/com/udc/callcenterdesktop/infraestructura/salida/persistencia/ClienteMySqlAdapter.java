/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Paquetes para Listas
import java.util.ArrayList;
import java.util.List;

// Paquetes del Dominio - Arquitectura)
import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

/**
 * Adaptador para base de datos MySQL.
 * Implementa el repositorio utilizando JDBC para realizar operaciones CRUD.
 */
public class ClienteMySqlAdapter implements IClienteRepository {

    // Sentencias SQL constantes para mantenimiento y legibilidad
    private static final String INSERT_SQL = "INSERT INTO clientes (nombre_completo, documento_identidad, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_SQL = "SELECT * FROM clientes";
    private static final String UPDATE_SQL = "UPDATE clientes SET nombre_completo=?, documento_identidad=?, telefono=?, email=?, direccion=? WHERE id_cliente=?";
    private static final String DELETE_SQL = "DELETE FROM clientes WHERE id_cliente=?";

    @Override
    public void guardar(Cliente c) {
        // Uso de try-with-resources para asegurar el cierre de conexiones
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            configurarStatement(stmt, c);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al guardar cliente en la base de datos", e);
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SELECT_SQL); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getLong("id_cliente"),
                    rs.getString("nombre_completo"),
                    rs.getString("documento_identidad"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion")
                ));
            }
            return lista;
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al consultar la lista de clientes", e);
        }
    }

    @Override
    public void actualizar(Cliente c) {
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            configurarStatement(stmt, c);
            stmt.setLong(6, c.getIdCliente()); // El ID es el último parámetro
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al actualizar los datos del cliente", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al eliminar el cliente", e);
        }
    }

    // Método auxiliar para asignar parámetros al PreparedStatement
    private void configurarStatement(PreparedStatement stmt, Cliente c) throws SQLException {
        stmt.setString(1, c.getNombreCompleto());
        stmt.setString(2, c.getDocumentoIdentidad());
        stmt.setString(3, c.getTelefono());
        stmt.setString(4, c.getEmail());
        stmt.setString(5, c.getDireccion());
    }
}