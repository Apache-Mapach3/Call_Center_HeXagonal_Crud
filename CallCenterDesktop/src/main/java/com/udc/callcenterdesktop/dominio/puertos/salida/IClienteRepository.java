package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import java.util.List;
import java.util.Optional; // IMPORTANTE

public interface IClienteRepository {
    void guardar(Cliente cliente);
    void actualizar(Cliente cliente);
    void eliminar(Long id);
    List<Cliente> listarTodos();
    
    // ESTA LÍNEA ES LA QUE ARREGLA TU ERROR:
    Optional<Cliente> buscarPorId(Long id);
}