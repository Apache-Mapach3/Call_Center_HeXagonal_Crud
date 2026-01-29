package com.udc.callcenterdesktop.dominio.puertos.salida;

import com.udc.callcenterdesktop.dominio.modelo.Campania;
import java.util.List;
import java.util.Optional;

public interface ICampaniaRepository {

    void guardar(Campania campania);

    List<Campania> listarTodos();

    Optional<Campania> buscarPorId(Long id);

    void eliminar(Long id);
}
