package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de Campaña - Versión KISS
 * SIMPLIFICADO: Implementación completa y funcional
 */
public class CampaniaService implements ICampaniaService {

    private final ICampaniaRepository campaniaRepository;

    public CampaniaService(ICampaniaRepository campaniaRepository) {
        this.campaniaRepository = campaniaRepository;
    }

    @Override
    public void guardar(CampaniaDTO dto) {
        Campania campania = new Campania();
        campania.setIdCampania(dto.getIdCampania());
        campania.setNombreCampania(dto.getNombreCampania());
        campania.setTipoCampania(dto.getTipoCampania());
        campania.setFechaInicio(dto.getFechaInicio());
        campania.setFechaFin(dto.getFechaFin());
        campania.setSupervisoresCargo(dto.getSupervisoresCargo());
        campania.setDescripcionObjetivos(dto.getDescripcionObjetivos());
        
        campaniaRepository.guardar(campania);
    }

    @Override
    public List<CampaniaDTO> listarTodas() {
        return campaniaRepository.listarTodos()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir Entidad -> DTO
    private CampaniaDTO convertirADTO(Campania c) {
        CampaniaDTO dto = new CampaniaDTO();
        dto.setIdCampania(c.getIdCampania());
        dto.setNombreCampania(c.getNombreCampania());
        dto.setTipoCampania(c.getTipoCampania());
        dto.setFechaInicio(c.getFechaInicio());
        dto.setFechaFin(c.getFechaFin());
        dto.setSupervisoresCargo(c.getSupervisoresCargo());
        dto.setDescripcionObjetivos(c.getDescripcionObjetivos());
        return dto;
    }
}