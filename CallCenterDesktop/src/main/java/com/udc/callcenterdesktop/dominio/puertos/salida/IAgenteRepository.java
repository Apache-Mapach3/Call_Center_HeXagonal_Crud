package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Agente;
import java.util.List;
import java.util.Optional; 

public interface IAgenteRepository {
    void guardar(Agente agente);
    void actualizar(Agente agente);
    void eliminar(Long id);
    List<Agente> listarTodos();
    
    Optional<Agente> buscarPorId(Long id);
}