/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

/**
 *
 * @author camolano
 */

    import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Campania;


// Si usas un Mapper estático, omite la clase. Si es una clase, debe ser similar a esto:
public class CampaniaMapper {

    // Método para convertir DTO a Entidad (Campania)
    public Campania toEntity(CampaniaDTO dto) {
        // 🛑 CORRECCIÓN: Usar el constructor de 8 parámetros y asegurar que el ID sea INT
        // El DTO puede tener el ID como Long, por lo que usamos .intValue()
        return new Campania(
            dto.getId().intValue(), // CORRECCIÓN CLAVE
            dto.getNombre(),
            dto.getTipoCampania(),
            dto.getFechaInicio(),
            dto.getFechaFin(),
            dto.getSupervisoresCargo(),
            dto.getDescripcionObjetivos(),
            dto.getEstado() // Nuevo parámetro 'estado'
        );
    }

    // Método para convertir Entidad (Campania) a DTO
    public CampaniaDTO toDTO(Campania entity) {
        CampaniaDTO dto = new CampaniaDTO();
        
        // 🛑 CORRECCIÓN: Usar los getters correctos (getId(), getNombre())
        dto.setId((long) entity.getId()); // CORRECCIÓN: Convertimos int a Long para el DTO
        dto.setNombre(entity.getNombre()); // CORRECCIÓN: Usar getNombre()
        dto.setTipoCampania(entity.getTipoCampania());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setFechaFin(entity.getFechaFin());
        dto.setSupervisoresCargo(entity.getSupervisoresCargo());
        dto.setDescripcionObjetivos(entity.getDescripcionObjetivos());
        dto.setEstado(entity.getEstado());
        return dto;
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

