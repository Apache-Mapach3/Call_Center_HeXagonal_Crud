/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.infraestructura.salida.persistencia;
/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Adaptador de Persistencia para MySQL.
 * Implementa el Puerto de Salida (IAgenteRepository) definido en el Dominio.
 * Utiliza JDBC puro para realizar operaciones CRUD sobre la tabla 'agentes'.
 */
public class AgenteMySqlAdapter implements IAgenteRepository {

    // Consultas SQL predefinidas para evitar errores de sintaxis y facilitar el mantenimiento
    private static final String INSERT_SQL = "INSERT INTO agentes (nombre_completo, numero_empleado, telefono_contacto, email, horario_turno, nivel_experiencia) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM agentes";
    private static final String UPDATE_SQL = "UPDATE agentes SET nombre_completo=?, numero_empleado=?, telefono_contacto=?, email=?, horario_turno=?, nivel_experiencia=? WHERE id_agente=?";
    private static final String DELETE_SQL = "DELETE FROM agentes WHERE id_agente=?";

    /**
     * Guarda un nuevo registro de Agente en la base de datos.
     * @param agente Objeto con la informacion a persistir.
     */
    @Override
    public void guardar(Agente agente) {
        // Se utiliza try-with-resources para asegurar que la conexion y el statement 
        // se cierren automaticamente, evitando fugas de memoria.
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            configurarStatement(stmt, agente);
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Agente registrado exitosamente.");
            
        } catch (SQLException e) {
            mostrarError("Error al guardar en base de datos", e);
        }
    }

    /**
     * Actualiza los datos de un Agente existente.
     * @param agente Objeto con la informacion modificada. Debe contener un ID valido.
     */
    @Override
    public void actualizar(Agente agente) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            configurarStatement(stmt, agente);
            // El ID es el septimo parametro en la consulta UPDATE (WHERE id_agente = ?)
            stmt.setLong(7, agente.getIdAgente());
            
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Agente actualizado correctamente.");
            
        } catch (SQLException e) {
            mostrarError("Error al actualizar el registro", e);
        }
    }

    /**
     * Elimina un Agente de la base de datos por su identificador.
     * @param id Identificador unico del agente a eliminar.
     */
    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Agente eliminado del sistema.");
            
        } catch (SQLException e) {
            mostrarError("Error al eliminar el registro", e);
        }
    }

    /**
     * Recupera todos los agentes almacenados en la base de datos.
     * @return Una lista de objetos Agente. Si no hay registros, retorna una lista vacia.
     */
    @Override
    public List<Agente> listarTodos() {
        List<Agente> lista = new ArrayList<>();
        
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            // Iteramos sobre el conjunto de resultados (filas de la tabla)
            while (rs.next()) {
                Agente a = new Agente(
                    rs.getLong("id_agente"),
                    rs.getString("nombre_completo"),
                    rs.getString("numero_empleado"),
                    rs.getString("telefono_contacto"),
                    rs.getString("email"),
                    rs.getString("horario_turno"),
                    rs.getString("nivel_experiencia")
                );
                lista.add(a);
            }
            
        } catch (SQLException e) {
            mostrarError("Error al listar los agentes", e);
        }
        return lista;
    }

    
    // METODOS PRIVADOS AUXILIARES 
    

    /**
     * Asigna los valores del objeto Agente a los parametros del PreparedStatement.
     * Se usa tanto en guardar como en actualizar para evitar duplicidad de codigo.
     */
    private void configurarStatement(PreparedStatement stmt, Agente agente) throws SQLException {
        stmt.setString(1, agente.getNombreCompleto());
        stmt.setString(2, agente.getNumeroEmpleado());
        stmt.setString(3, agente.getTelefonoContacto());
        stmt.setString(4, agente.getEmail());
        stmt.setString(5, agente.getHorarioTurno());
        stmt.setString(6, agente.getNivelExperiencia());
    }
    
    /**
     * Centraliza el manejo de errores SQL para mostrar mensajes consistentes.
     */
    private void mostrarError(String titulo, SQLException e) {
        System.err.println(titulo + ": " + e.getMessage());
        JOptionPane.showMessageDialog(null, 
                titulo + ": " + e.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
    }
}