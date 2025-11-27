/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador de persistencia para la entidad Cliente usando MySQL.
 * Implementa el patrón Repository con JDBC.
 * 
 * <p>Proporciona operaciones CRUD completas con manejo robusto
 * de errores y validaciones defensivas.</p>
 * 
 * @author Carlos 
 * @version 2.0
 * @since 2025
 */
public class ClienteMySqlAdapter implements IClienteRepository {

    // CONSTANTES SQL 
    
    private static final String SQL_INSERT = 
        "INSERT INTO clientes (nombre_completo, documento_identidad, telefono, email, direccion) " +
        "VALUES (?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT id_cliente, nombre_completo, documento_identidad, telefono, email, direccion " +
        "FROM clientes " +
        "ORDER BY nombre_completo";
    
    private static final String SQL_UPDATE = 
        "UPDATE clientes SET " +
        "nombre_completo = ?, " +
        "documento_identidad = ?, " +
        "telefono = ?, " +
        "email = ?, " +
        "direccion = ? " +
        "WHERE id_cliente = ?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM clientes WHERE id_cliente = ?";

    /**
     * Persiste un nuevo cliente en la base de datos.
     * 
     * @param cliente entidad de dominio a guardar
     * @throws IllegalArgumentException si el cliente es null
     * @throws CallCenterException si ocurre un error de persistencia
     */
    @Override
    public void guardar(Cliente cliente) {
        validarClienteNoNulo(cliente);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatementGuardar(stmt, cliente);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "No se pudo guardar el cliente en la base de datos"
                );
            }
            
            // Obtener el ID generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al guardar cliente: " + e.getMessage(), e
            );
        }
    }

    /**
     * Recupera todos los clientes registrados.
     * 
     * @return lista inmutable de clientes
     * @throws CallCenterException si ocurre un error de lectura
     */
    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL); 
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
            return Collections.unmodifiableList(clientes);
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al consultar la lista de clientes: " + e.getMessage(), e
            );
        }
    }

    /**
     * Actualiza los datos de un cliente existente.
     * 
     * @param cliente entidad con datos actualizados
     * @throws IllegalArgumentException si el cliente es null o no tiene ID
     * @throws CallCenterException si el cliente no existe o hay error de BD
     */
    @Override
    public void actualizar(Cliente cliente) {
        validarClienteNoNulo(cliente);
        validarIdCliente(cliente.getIdCliente());
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            
            configurarStatementActualizar(stmt, cliente);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "Cliente con ID " + cliente.getIdCliente() + " no encontrado"
                );
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al actualizar los datos del cliente: " + e.getMessage(), e
            );
        }
    }

    /**
     * Elimina un cliente del sistema de forma permanente.
     * 
     * @param id identificador único del cliente
     * @throws IllegalArgumentException si el ID es null o inválido
     * @throws CallCenterException si el cliente no existe o hay error de BD
     */
    @Override
    public void eliminar(Long id) {
        validarIdCliente(id);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            
            stmt.setLong(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException(
                    "Cliente con ID " + id + " no encontrado"
                );
            }
            
        } catch (SQLException e) {
            throw new CallCenterException(
                "Error al eliminar el cliente: " + e.getMessage(), e
            );
        }
    }

    
    // MÉTODOS PRIVADOS DE UTILIDAD
    

    private void configurarStatementGuardar(PreparedStatement stmt, Cliente cliente) 
            throws SQLException {
        stmt.setString(1, cliente.getNombreCompleto());
        stmt.setString(2, cliente.getDocumentoIdentidad());
        stmt.setString(3, cliente.getTelefono());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, cliente.getDireccion());
    }

    private void configurarStatementActualizar(PreparedStatement stmt, Cliente cliente) 
            throws SQLException {
        configurarStatementGuardar(stmt, cliente);
        stmt.setLong(6, cliente.getIdCliente());
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getLong("id_cliente"),
            rs.getString("nombre_completo"),
            rs.getString("documento_identidad"),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getString("direccion")
        );
    }

    private void validarClienteNoNulo(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser null");
        }
    }

    private void validarIdCliente(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido: " + id);
        }
    }
}