package com.udc.callcenterdesktop.infraestructura.salida.persistencia;

import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteMySqlAdapter implements IClienteRepository {

    private static final String INSERT =
            "INSERT INTO clientes (nombre_completo, documento_identidad, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL =
            "SELECT * FROM clientes ORDER BY nombre_completo";

    // 1. Agregamos esta constante que faltaba
    private static final String SELECT_BY_ID =
            "SELECT * FROM clientes WHERE id_cliente=?";

    private static final String UPDATE =
            "UPDATE clientes SET nombre_completo=?, documento_identidad=?, telefono=?, email=?, direccion=? WHERE id_cliente=?";

    private static final String DELETE =
            "DELETE FROM clientes WHERE id_cliente=?";

    @Override
    public void guardar(Cliente c) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            mapPreparedStatement(st, c);
            st.executeUpdate();

            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCliente(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al guardar cliente");
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            throw new CallCenterException("Error al listar clientes");
        }

        return lista;
    }

    @Override
    public void actualizar(Cliente c) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(UPDATE)) {

            mapPreparedStatement(st, c);
            st.setLong(6, c.getIdCliente());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new CallCenterException("Error al actualizar cliente");
        }
    }

    @Override
    public void eliminar(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(DELETE)) {

            st.setLong(1, id);
            st.executeUpdate();

        } catch (SQLException e) {
            throw new CallCenterException("Error al eliminar cliente");
        }
    }

    // 2. Implementación REAL del método buscarPorId
    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement st = conn.prepareStatement(SELECT_BY_ID)) {
            
            st.setLong(1, id);
            
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    // Reutilizamos mapResultSet para convertir la fila en objeto
                    return Optional.of(mapResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new CallCenterException("Error al buscar cliente por ID: " + e.getMessage());
        }
        
        return Optional.empty(); // Retorna vacío si no lo encuentra
    }

    private void mapPreparedStatement(PreparedStatement st, Cliente c) throws SQLException {
        st.setString(1, c.getNombreCompleto());
        st.setString(2, c.getDocumentoIdentidad());
        st.setString(3, c.getTelefono());
        st.setString(4, c.getEmail());
        st.setString(5, c.getDireccion());
    }

    private Cliente mapResultSet(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getLong("id_cliente"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setDocumentoIdentidad(rs.getString("documento_identidad"));
        c.setTelefono(rs.getString("telefono"));
        c.setEmail(rs.getString("email"));
        c.setDireccion(rs.getString("direccion"));
        return c;
    }
}