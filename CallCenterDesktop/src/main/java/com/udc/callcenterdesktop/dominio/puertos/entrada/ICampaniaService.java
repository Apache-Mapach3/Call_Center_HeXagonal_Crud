package com.udc.callcenterdesktop.dominio.puertos.entrada;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import java.util.List;

public interface ICampaniaService {

    void guardar(CampaniaDTO dto);

    List<CampaniaDTO> listarTodas();
}
