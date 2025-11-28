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
 * Adaptador de persistencia para la entidad Cliente.
 * Implementa el patrón Repository utilizando JDBC estándar.
 * * <p>Esta clase funciona como el puente técnico entre el Dominio y la Base de Datos.
 * Aunque el nombre refiere a MySQL, el uso de JDBC estándar permite compatibilidad 
 * con <b>SQLite</b> y otros motores relacionales.</p>
 * * <p><b>Características de Calidad:</b></p>
 * <ul>
 * <li><b>Seguridad:</b> Uso de PreparedStatements contra inyección SQL.</li>
 * <li><b>Integridad:</b> Manejo de restricciones de base de datos (Unique/Foreign Keys).</li>
 * <li><b>Eficiencia:</b> Gestión automática de recursos con try-with-resources.</li>
 * </ul>
 * * @author Infraestructura Team
 * @version 2.1
 * @since 2025
 */
public class ClienteMySqlAdapter implements IClienteRepository {


    // CONSTANTES SQL (Consultas Precompiladas)

    private static final String SQL_INSERT = 
        "INSERT INTO clientes (nombre_completo, documento_identidad, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT id_cliente, nombre_completo, documento_identidad, telefono, email, direccion FROM clientes ORDER BY nombre_completo";
    
    private static final String SQL_UPDATE = 
        "UPDATE clientes SET nombre_completo = ?, documento_identidad = ?, telefono = ?, email = ?, direccion = ? WHERE id_cliente = ?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM clientes WHERE id_cliente = ?";

    private static final String SQL_SELECT_BY_ID = 
        "SELECT id_cliente, nombre_completo, documento_identidad, telefono, email, direccion FROM clientes WHERE id_cliente = ?";


    // IMPLEMENTACIÓN DE MÉTODOS CRUD

    /**
     * Persiste un nuevo cliente en la base de datos.
     * Recupera automáticamente el ID generado por el motor de BD.
     * * @param cliente entidad de dominio a guardar
     * @throws IllegalArgumentException si el cliente es null
     * @throws CallCenterException si ocurre un error de persistencia o duplicidad
     */
    @Override
    public void guardar(Cliente cliente) {
        validarClienteNoNulo(cliente);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            
            configurarStatementGuardar(stmt, cliente);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException("No se pudo guardar el cliente, no se creó el registro.");
            }
            
            // Recuperar ID generado (Auto-increment)
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setIdCliente(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            // Manejo específico para Documento de Identidad duplicado (SQLite/MySQL)
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("Duplicate")) {
                throw new CallCenterException("El documento de identidad '" + cliente.getDocumentoIdentidad() + "' ya se encuentra registrado.");
            }
            throw new CallCenterException("Error de base de datos al guardar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera todos los clientes registrados.
     * * @return lista inmutable de clientes para proteger la integridad de los datos en memoria.
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
            throw new CallCenterException("Error al consultar la lista de clientes", e);
        }
    }

    /**
     * Actualiza los datos de un cliente existente.
     * * @param cliente entidad con datos actualizados (debe incluir ID)
     * @throws IllegalArgumentException si el cliente es null o no tiene ID
     */
    @Override
    public void actualizar(Cliente cliente) {
        validarClienteNoNulo(cliente);
        validarIdCliente(cliente.getIdCliente());
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {
            
            configurarStatementGuardar(stmt, cliente);
            stmt.setLong(6, cliente.getIdCliente()); // El ID es el parámetro 6
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException("No se encontró el cliente con ID " + cliente.getIdCliente() + " para actualizar.");
            }
            
        } catch (SQLException e) {
            // Validar si al actualizar se duplica el documento de otro cliente
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("Duplicate")) {
                throw new CallCenterException("El documento de identidad ya pertenece a otro cliente.");
            }
            throw new CallCenterException("Error al actualizar los datos del cliente", e);
        }
    }

    /**
     * Elimina un cliente del sistema de forma permanente.
     * * @param id identificador único del cliente
     * @throws CallCenterException si el cliente tiene registros asociados (Integridad Referencial)
     */
    @Override
    public void eliminar(Long id) {
        validarIdCliente(id);
        
        try (Connection conn = ConexionDB.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {
            
            stmt.setLong(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new CallCenterException("No se encontró el cliente con ID " + id + " para eliminar.");
            }
            
        } catch (SQLException e) {
            // Manejo de restricción de clave foránea (Si el cliente tiene historial de llamadas)
            if (e.getMessage().contains("FOREIGN KEY")) {
                throw new CallCenterException("No se puede eliminar el cliente porque tiene historial de llamadas asociado.");
            }
            throw new CallCenterException("Error al eliminar el cliente", e);
        }
    }

 
    public Cliente buscarPorId(Long id) {
        validarIdCliente(id);
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new CallCenterException("Error al buscar cliente por ID", e);
        }
    }

    // MÉTODOS PRIVADOS DE UTILIDAD

    /**
     * Configura los parámetros del PreparedStatement para Insertar/Actualizar.
     */
    private void configurarStatementGuardar(PreparedStatement stmt, Cliente cliente) throws SQLException {
        stmt.setString(1, cliente.getNombreCompleto());
        stmt.setString(2, cliente.getDocumentoIdentidad());
        stmt.setString(3, cliente.getTelefono());
        stmt.setString(4, cliente.getEmail());
        stmt.setString(5, cliente.getDireccion());
    }

    /**
     * Mapea un ResultSet a una entidad Cliente utilizando Setters.
     * Esto desacopla el orden del constructor de la consulta SQL.
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getLong("id_cliente"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setDocumentoIdentidad(rs.getString("documento_identidad"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setDireccion(rs.getString("direccion"));
        return c;
    }

    private void validarClienteNoNulo(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("La entidad Cliente no puede ser null.");
        }
    }

    private void validarIdCliente(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de cliente inválido: " + id);
        }
    }
}