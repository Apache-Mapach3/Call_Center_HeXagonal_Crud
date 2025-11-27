/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.dominio.modelo.Agente;

/**
 * Mapper para conversiones entre Agente (Entidad de Dominio) y AgenteDTO.
 * 
 * <p>Este mapper implementa el patrón de transformación de datos,
 * permitiendo convertir entre objetos de diferentes capas sin crear
 * acoplamiento directo.</p>
 * 
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Convertir entidades de dominio a DTOs (para la vista)</li>
 *   <li>Convertir DTOs a entidades de dominio (para persistencia)</li>
 *   <li>Manejar valores null de forma segura</li>
 * </ul>
 * 
 * <p><b>Nota:</b> Este mapper es stateless (sin estado), por lo que
 * todos sus métodos son estáticos.</p>
 * 
 * @author Jose 
 * @version 2.0
 * @since 2025
 */
public class AgenteMapper {

    /**
     * Constructor privado para evitar instanciación.
     * Esta clase solo contiene métodos estáticos.
     */
    private AgenteMapper() {
        throw new UnsupportedOperationException(
            "Esta es una clase utilitaria y no debe ser instanciada"
        );
    }

    /**
     * Convierte un DTO a una Entidad de Dominio.
     * 
     * <p>Este método se usa cuando los datos vienen desde la capa de
     * presentación (formularios) y necesitan ser procesados por la
     * lógica de negocio.</p>
     * 
     * @param dto objeto de transferencia de datos
     * @return entidad de dominio correspondiente, o null si el DTO es null
     */
    public static Agente toEntity(AgenteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new Agente(
            dto.getId(),
            dto.getNombre(),
            dto.getNumeroEmpleado(),
            dto.getTelefono(),
            dto.getEmail(),
            dto.getTurno(),
            dto.getExperiencia()
        );
    }

    /**
     * Convierte una Entidad de Dominio a un DTO.
     * 
     * <p>Este método se usa cuando los datos vienen desde la capa de
     * persistencia y necesitan ser mostrados en la interfaz gráfica.</p>
     * 
     * @param entity entidad de dominio
     * @return DTO correspondiente, o null si la entidad es null
     */
    public static AgenteDTO toDTO(Agente entity) {
        if (entity == null) {
            return null;
        }
        
        return new AgenteDTO(
            entity.getIdAgente(),
            entity.getNombreCompleto(),
            entity.getNumeroEmpleado(),
            entity.getTelefonoContacto(),
            entity.getEmail(),
            entity.getHorarioTurno(),
            entity.getNivelExperiencia()
        );
    }
}