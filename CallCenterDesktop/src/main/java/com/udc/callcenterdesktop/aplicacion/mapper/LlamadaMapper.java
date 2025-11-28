/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.mapper;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;

/**
 * Clase utilitaria encargada de la transformación de datos entre capas.
 * Convierte objetos DTO (Data Transfer Objects) en Entidades de Dominio y viceversa.
 * * <p><b>Patrón de Diseño:</b> Mapper / Translator.</p>
 * * <p><b>Propósito:</b> Desacoplar la capa de presentación (Vista) de la capa de 
 * dominio (Negocio). La vista solo conoce los DTOs, mientras que el negocio 
 * solo trabaja con Entidades.</p>
 * * <p><b>Características:</b></p>
 * <ul>
 * <li>Clase estática (no requiere instanciación).</li>
 * <li>Manejo seguro de valores nulos (Null Safety).</li>
 * <li>Transferencia pura de datos sin lógica de negocio.</li>
 * </ul>
 * * @author Carlos
 * @version 2.0
 * @since 2025
 */
public class LlamadaMapper {

    /**
     * Constructor privado para evitar la instanciación de la clase.
     * Al ser una clase de utilidad (Utility Class), todos sus métodos son estáticos.
     */
    private LlamadaMapper() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada.");
    }

    /**
     * Transforma un DTO (proveniente de la interfaz gráfica) en una Entidad de Dominio.
     * * <p>Se utiliza cuando el usuario envía datos desde un formulario para ser procesados
     * por la lógica de negocio o guardados en la base de datos.</p>
     * * @param dto el objeto de transferencia con los datos de la llamada
     * @return la entidad {@link Llamada} poblada, o {@code null} si el DTO es nulo
     */
    public static Llamada toEntity(LlamadaDTO dto) {
        if (dto == null) {
            return null;
        }

        Llamada llamada = new Llamada();
        
        // Mapeo de atributos básicos
        llamada.setIdLlamada(dto.getIdLlamada());
        llamada.setFechaHora(dto.getFechaHora());
        llamada.setDuracion(dto.getDuracion());
        llamada.setDetalleResultado(dto.getDetalleResultado());
        llamada.setObservaciones(dto.getObservaciones());
        
        // Mapeo de claves foráneas (Relaciones)
        llamada.setIdAgente(dto.getIdAgente());
        llamada.setIdCliente(dto.getIdCliente());
        llamada.setIdCampania(dto.getIdCampania());

        return llamada;
    }

    /**
     * Transforma una Entidad de Dominio (proveniente de la BD o Lógica) en un DTO.
     * * <p>Se utiliza para enviar datos desde el núcleo del sistema hacia la interfaz gráfica
     * sin exponer la estructura interna de las entidades.</p>
     * * @param entity la entidad de dominio {@link Llamada}
     * @return el objeto {@link LlamadaDTO} listo para la vista, o {@code null} si la entidad es nula
     */
    public static LlamadaDTO toDTO(Llamada entity) {
        if (entity == null) {
            return null;
        }

        LlamadaDTO dto = new LlamadaDTO();
        
        // Transferencia de datos básicos
        dto.setIdLlamada(entity.getIdLlamada());
        dto.setFechaHora(entity.getFechaHora());
        dto.setDuracion(entity.getDuracion());
        dto.setDetalleResultado(entity.getDetalleResultado());
        dto.setObservaciones(entity.getObservaciones());
        
        // Transferencia de IDs relacionales
        dto.setIdAgente(entity.getIdAgente());
        dto.setIdCliente(entity.getIdCliente());
        dto.setIdCampania(entity.getIdCampania());
        
        // NOTA IMPORTANTE:
        // Los campos de nombres (nombreAgente, nombreCliente, etc.) generalmente no se llenan
        // en este mapeo simple, ya que la entidad 'Llamada' solo guarda los IDs.
        // Esos nombres suelen venir de consultas SQL con JOINs en el Repositorio 
        // y se asignan manualmente en casos de uso de "Listar Historial".

        return dto;
    }
}