/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.LlamadaMapper;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;

public class LlamadaService implements ILlamadaService {

    
    private final ILlamadaRepository llamadaRepository;
    private final IAgenteRepository agenteRepository;
    private final ICampaniaRepository campaniaRepository;
    private final IClienteRepository clienteRepository;
    private final LlamadaMapper llamadaMapper = new LlamadaMapper();

    
    public LlamadaService(ILlamadaRepository llamadaRepository, 
                          IAgenteRepository agenteRepository, 
                          ICampaniaRepository campaniaRepository, 
                          IClienteRepository clienteRepository) {
        this.llamadaRepository = llamadaRepository;
        this.agenteRepository = agenteRepository;
        this.campaniaRepository = campaniaRepository;
        this.clienteRepository = clienteRepository;
    }

    
    private LlamadaDTO enriquecerLlamadaDTO(Llamada llamada) {
        LlamadaDTO dto = llamadaMapper.toDTO(llamada);
        
        
 
       
        campaniaRepository.buscarPorId(llamada.getIdCampania())
            .ifPresent(campania -> dto.setNombreCampania(campania.getNombre()));
        
        
        clienteRepository.buscarPorId(llamada.getIdCliente())
            .ifPresent(cliente -> dto.setNombreCliente(cliente.getNombre()));

        return dto;
    }


    public LlamadaDTO crearOActualizarLlamada(LlamadaDTO llamadaDTO) {
      
        
        Llamada llamada = llamadaMapper.toEntity(llamadaDTO);
        Llamada guardada = llamadaRepository.guardar(llamada);
        
        return enriquecerLlamadaDTO(guardada);
    }


    public Optional<LlamadaDTO> obtenerLlamadaPorId(int id) {
        return llamadaRepository.buscarPorId(id)
            .map(this::enriquecerLlamadaDTO);
    }

   
    public List<LlamadaDTO> obtenerTodasLlamadas() {
        
        List<Llamada> llamadas = llamadaRepository.buscarTodas();
        
       
        return llamadas.stream()
            .map(this::enriquecerLlamadaDTO)
            .collect(Collectors.toList());
    }


    public boolean eliminarLlamada(int id) {
        return llamadaRepository.eliminarPorId(id);
    }
}
