package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService; // <--- IMPORTANTE
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;

import java.util.List;

// 1. Aquí decimos que esta clase CUMPLE con el contrato de ILlamadaService
public class LlamadaService implements ILlamadaService {

    // 2. Aquí declaramos la variable del repositorio correctamente
    private final ILlamadaRepository llamadaRepository;

    public LlamadaService(ILlamadaRepository llamadaRepository) {
        this.llamadaRepository = llamadaRepository;
    }


    public List<Llamada> listarTodas() {
        return llamadaRepository.listarTodas();
    }

    @Override
    public void registrar(LlamadaDTO dto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<LlamadaDTO> listarLlamadas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}