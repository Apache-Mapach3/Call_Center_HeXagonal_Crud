/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import java.util.List;
/**
 *
 * @author camolano
 */
/**
 * Puerto de Entrada (Input Port).
 * Define qué operaciones de negocio ofrece el sistema.
 */
public interface ICampaniaService {
    void registrarCampania(CampaniaDTO dto);
    List<CampaniaDTO> listarCampanias();
    void actualizarCampania(CampaniaDTO dto);
    void eliminarCampania(Long id);
}

