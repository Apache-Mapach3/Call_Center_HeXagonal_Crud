package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CampaniaService implements ICampaniaService {

    private final ICampaniaRepository campaniaRepository;

    public CampaniaService(ICampaniaRepository campaniaRepository) {
        this.campaniaRepository = campaniaRepository;
    }

    @Override
    public void guardar(CampaniaDTO dto) {
        Campania campania = new Campania(
                dto.getIdCampania(),
                dto.getNombreCampania(),
                dto.getDescripcion()
        );
        campaniaRepository.guardar(campania);
    }

    @Override
public List<CampaniaDTO> listarTodas() {
    return campaniaRepository.listarTodos()
            .stream()
            .map(c -> {
                CampaniaDTO dto = new CampaniaDTO();
                dto.setIdCampania(c.getIdCampania());
                dto.setNombreCampania(c.getNombreCampania());
                dto.setDescripcion(c.getDescripcionCampania()); 
                return dto;
            })
            .collect(Collectors.toList());
    }
}
