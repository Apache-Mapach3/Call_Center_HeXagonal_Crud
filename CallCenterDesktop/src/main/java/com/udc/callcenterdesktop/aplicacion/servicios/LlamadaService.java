/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.LlamadaDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.LlamadaMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Llamada;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.ILlamadaRepository;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ILlamadaService;

import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;  
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;  
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository; 

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para el registro y gestión de Llamadas.
 * Implementa la lógica de negocio transaccional del Call Center.
 * 
 * <p>Este servicio coordina el registro de llamadas entre agentes,
 * clientes y campañas, aplicando validaciones críticas de integridad
 * referencial y reglas de negocio específicas.</p>
 * 
 * <p><b>Validaciones de Integridad Referencial:</b></p>
 * <ul>
 *   <li>Verifica que el Agente exista antes de registrar la llamada</li>
 *   <li>Verifica que el Cliente exista antes de registrar la llamada</li>
 *   <li>Verifica que la Campaña exista antes de registrar la llamada</li>
 *   <li>Previene errores de Foreign Key en SQLite</li>
 * </ul>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>Una llamada debe estar asociada a un agente, cliente y campaña válidos</li>
 *   <li>La duración debe ser mayor a 0 segundos</li>
 *   <li>El detalle del resultado es obligatorio</li>
 *   <li>La duración máxima permitida es de 4 horas (14400 segundos)</li>
 *   <li>No se permiten llamadas con fecha futura</li>
 * </ul>
 * 
 * @author Carlos Martinez
 * @version 3.0 - Con validaciones de integridad referencial
 * @since 2025
 */
public class LlamadaService implements ILlamadaService {

    // Constantes de validación
    private static final int DURACION_MINIMA_SEGUNDOS = 1;
    private static final int DURACION_MAXIMA_SEGUNDOS = 14400; // 4 horas
    private static final int MIN_LONGITUD_DETALLE = 3;
    private static final int MAX_LONGITUD_DETALLE = 500;
    private static final int MAX_LONGITUD_OBSERVACIONES = 1000;
    
    // Repositorio principal
    private final ILlamadaRepository repositorio;
    
    // Repositorios adicionales para validaciones cruzadas
    private final IAgenteRepository agenteRepository;
    private final IClienteRepository clienteRepository;
    private final ICampaniaRepository campaniaRepository;

    /**
     * Constructor con inyección de dependencias múltiples.
     * 
     * <p>Este constructor recibe los repositorios necesarios para realizar
     * validaciones de integridad referencial antes de persistir una llamada.</p>
     * 
     * @param repositorio repositorio de llamadas (persistencia principal)
     * @param agenteRepo repositorio de agentes (para validar existencia)
     * @param clienteRepo repositorio de clientes (para validar existencia)
     * @param campaniaRepo repositorio de campañas (para validar existencia)
     * @throws IllegalArgumentException si algún repositorio es null
     */
    public LlamadaService(
            ILlamadaRepository repositorio,
            IAgenteRepository agenteRepo,
            IClienteRepository clienteRepo,
            ICampaniaRepository campaniaRepo) {
        
        // Validar que ningún repositorio sea null
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio de llamadas no puede ser null");
        }
        if (agenteRepo == null) {
            throw new IllegalArgumentException("El repositorio de agentes no puede ser null");
        }
        if (clienteRepo == null) {
            throw new IllegalArgumentException("El repositorio de clientes no puede ser null");
        }
        if (campaniaRepo == null) {
            throw new IllegalArgumentException("El repositorio de campañas no puede ser null");
        }
        
        this.repositorio = repositorio;
        this.agenteRepository = agenteRepo;
        this.clienteRepository = clienteRepo;
        this.campaniaRepository = campaniaRepo;
        
        
    }

    /**
     * Registra una nueva llamada en el sistema.
     * 
     * <p>Este método es transaccional y crítico para el negocio.
     * Valida exhaustivamente la integridad referencial y las reglas
     * de negocio antes de persistir.</p>
     * 
     * <p><b>Validaciones críticas realizadas:</b></p>
     * <ul>
     *   <li>Existencia de agente, cliente y campaña en BD</li>
     *   <li>Duración dentro de rangos permitidos</li>
     *   <li>Detalle del resultado completo</li>
     *   <li>Fecha/hora coherente (no futura)</li>
     * </ul>
     * 
     * @param dto objeto de transferencia con los datos de la llamada
     * @throws IllegalArgumentException si el DTO es null
     * @throws CallCenterException si las validaciones fallan
     */
    @Override
public void registrarLlamada(LlamadaDTO dto) {
    // 1. Validar DTO no nulo
    validarDTONoNulo(dto);
    
    // 2. Validar IDs presentes
    validarIntegridadReferencial(dto);
    
    // 3. ✅ VALIDAR EXISTENCIA EN BD (CRÍTICO)
    validarExistenciaEntidadesRelacionadas(dto);
    
    // 4. Convertir DTO → Entidad (AQUÍ USAS TU MAPPER)
    Llamada entidad = LlamadaMapper.toEntity(dto);
    
    // 5. Validar reglas de negocio
    validarReglasDeNegocio(entidad);
    
    // 6. Persistir (LLAMAR AL REPOSITORIO)
    repositorio.registrar(entidad); // ← AQUÍ SE LLAMA AL ADAPTER
}

    /**
     * Recupera el historial completo de llamadas con información enriquecida.
     * 
     * <p>Este método retorna DTOs que incluyen los nombres de agentes,
     * clientes y campañas mediante JOINs en la consulta SQL, evitando
     * el problema N+1 y mejorando el rendimiento.</p>
     * 
     * @return lista de DTOs con historial completo de llamadas
     */
    @Override
    public List<LlamadaDTO> listarHistorial() {
        return repositorio.listarLlamadasConNombres();
    }
    
    
    // MÉTODOS PRIVADOS DE VALIDACIÓN DE INTEGRIDAD REFERENCIAL   
    /**
     * Valida que el DTO no sea null.
     * 
     * @param dto objeto a validar
     * @throws IllegalArgumentException si el DTO es null
     */
    private void validarDTONoNulo(LlamadaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException(
                "El DTO de la llamada no puede ser null"
            );
        }
    }
    
    /**
     * Valida la integridad referencial básica (IDs presentes).
     * 
     * <p>Verifica que existan IDs válidos para agente, cliente y campaña.
     * Esta validación es CRÍTICA porque una llamada sin estas relaciones
     * quedaría huérfana en el sistema.</p>
     * 
     * @param dto DTO con los IDs a validar
     * @throws CallCenterException si falta algún ID o es inválido
     */
    private void validarIntegridadReferencial(LlamadaDTO dto) {
        StringBuilder errores = new StringBuilder();
        
        if (dto.getIdAgente() == null || dto.getIdAgente() <= 0) {
            errores.append("- Debe seleccionar un Agente válido\n");
        }
        
        if (dto.getIdCliente() == null || dto.getIdCliente() <= 0) {
            errores.append("- Debe seleccionar un Cliente válido\n");
        }
        
        if (dto.getIdCampania() == null || dto.getIdCampania() <= 0) {
            errores.append("- Debe seleccionar una Campaña válida\n");
        }
        
        if (errores.length() > 0) {
            throw new CallCenterException(
                "Error de integridad referencial:\n" + errores.toString()
            );
        }
    }
    
    /**
     * VALIDACIÓN CRÍTICA: Verifica que las entidades relacionadas existan en BD.
     * 
     * <p>Esta validación previene errores de Foreign Key en SQLite al intentar
     * insertar una llamada que referencia a un Agente, Cliente o Campaña inexistente.</p>
     * 
     * <p><b>¿Por qué es necesaria?</b></p>
     * <ul>
     *   <li>SQLite puede tener las Foreign Keys deshabilitadas</li>
     *   <li>Previene datos huérfanos en la BD</li>
     *   <li>Proporciona mensajes de error amigables al usuario</li>
     *   <li>Evita excepciones técnicas de SQL que son difíciles de entender</li>
     * </ul>
     * 
     * @param dto DTO con los IDs de las entidades relacionadas
     * @throws CallCenterException si alguna entidad no existe
     */
    private void validarExistenciaEntidadesRelacionadas(LlamadaDTO dto) {
        StringBuilder errores = new StringBuilder();
        
        // Validar existencia del Agente
        boolean agenteExiste = agenteRepository.listarTodos()
                .stream()
                .anyMatch(a -> a.getIdAgente().equals(dto.getIdAgente()));
        
        if (!agenteExiste) {
            errores.append("- El Agente seleccionado (ID: ")
                   .append(dto.getIdAgente())
                   .append(") no existe en el sistema\n");
        }
        
        // Validar existencia del Cliente
        boolean clienteExiste = clienteRepository.listarTodos()
                .stream()
                .anyMatch(c -> c.getIdCliente().equals(dto.getIdCliente()));
        
        if (!clienteExiste) {
            errores.append("- El Cliente seleccionado (ID: ")
                   .append(dto.getIdCliente())
                   .append(") no existe en el sistema\n");
        }
        
        // Validar existencia de la Campaña
        boolean campaniaExiste = campaniaRepository.listarTodos()
                .stream()
                .anyMatch(c -> c.getIdCampania().equals(dto.getIdCampania()));
        
        if (!campaniaExiste) {
            errores.append("- La Campaña seleccionada (ID: ")
                   .append(dto.getIdCampania())
                   .append(") no existe en el sistema\n");
        }
        
        // Si hay errores, lanzar excepción con todos los problemas encontrados
        if (errores.length() > 0) {
            throw new CallCenterException(
                "Error de integridad referencial - Entidades no encontradas:\n\n" + 
                errores.toString() +
                "\nPor favor, verifique que los registros de Agente, Cliente y Campaña " +
                "existan antes de registrar la llamada."
            );
        }
    }
    
    
    // MÉTODOS PRIVADOS DE VALIDACIÓN DE REGLAS DE NEGOCIO

    
    /**
     * Aplica todas las reglas de negocio sobre la entidad Llamada.
     * 
     * @param llamada entidad a validar
     * @throws CallCenterException si alguna validación falla
     */
    private void validarReglasDeNegocio(Llamada llamada) {
        validarFechaHora(llamada.getFechaHora());
        validarDuracion(llamada.getDuracion());
        validarDetalleResultado(llamada.getDetalleResultado());
        validarObservaciones(llamada.getObservaciones());
    }
    
    /**
     * Valida que la fecha/hora sea coherente.
     * 
     * @param fechaHora fecha y hora de la llamada
     * @throws CallCenterException si la fecha es futura o muy antigua
     */
    private void validarFechaHora(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new CallCenterException(
                "La fecha y hora de la llamada es obligatoria"
            );
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        
        // No permitir fechas futuras
        if (fechaHora.isAfter(ahora)) {
            throw new CallCenterException(
                "No se puede registrar una llamada con fecha futura"
            );
        }
        
        // Advertir si la llamada es muy antigua (más de 1 año)
        LocalDateTime haceUnAnio = ahora.minusYears(1);
        if (fechaHora.isBefore(haceUnAnio)) {
            throw new CallCenterException(
                "La fecha de la llamada es demasiado antigua (más de 1 año). " +
                "Verifique que la fecha sea correcta."
            );
        }
    }
    
    /**
     * Valida que la duración esté dentro de rangos permitidos.
     * 
     * @param duracion duración en segundos
     * @throws CallCenterException si la duración es inválida
     */
    private void validarDuracion(Integer duracion) {
        if (duracion == null) {
            throw new CallCenterException(
                "La duración de la llamada es obligatoria"
            );
        }
        
        if (duracion < DURACION_MINIMA_SEGUNDOS) {
            throw new CallCenterException(
                String.format("La duración debe ser mayor a %d segundos", 
                    DURACION_MINIMA_SEGUNDOS)
            );
        }
        
        if (duracion > DURACION_MAXIMA_SEGUNDOS) {
            throw new CallCenterException(
                String.format("La duración no puede exceder %d segundos (%d horas). " +
                    "Si la llamada fue realmente tan larga, divídala en múltiples registros.",
                    DURACION_MAXIMA_SEGUNDOS, DURACION_MAXIMA_SEGUNDOS / 3600)
            );
        }
    }
    
    /**
     * Valida el detalle del resultado de la llamada.
     * 
     * @param detalle descripción del resultado
     * @throws CallCenterException si el detalle es inválido
     */
    private void validarDetalleResultado(String detalle) {
        if (detalle == null || detalle.trim().isEmpty()) {
            throw new CallCenterException(
                "El detalle del resultado de la llamada es obligatorio. " +
                "Indique el resultado: Venta realizada, No contesta, Rechazado, etc."
            );
        }
        
        String detalleLimpio = detalle.trim();
        
        if (detalleLimpio.length() < MIN_LONGITUD_DETALLE) {
            throw new CallCenterException(
                String.format("El detalle debe tener al menos %d caracteres", 
                    MIN_LONGITUD_DETALLE)
            );
        }
        
        if (detalleLimpio.length() > MAX_LONGITUD_DETALLE) {
            throw new CallCenterException(
                String.format("El detalle no puede exceder %d caracteres. " +
                    "Use el campo de observaciones para información adicional.",
                    MAX_LONGITUD_DETALLE)
            );
        }
    }
    
    /**
     * Valida las observaciones adicionales si se proporcionan.
     * 
     * @param observaciones notas adicionales (opcional)
     * @throws CallCenterException si excede el límite de caracteres
     */
    private void validarObservaciones(String observaciones) {
        // Las observaciones son opcionales
        if (observaciones == null || observaciones.trim().isEmpty()) {
            return;
        }
        
        if (observaciones.length() > MAX_LONGITUD_OBSERVACIONES) {
            throw new CallCenterException(
                String.format("Las observaciones no pueden exceder %d caracteres",
                    MAX_LONGITUD_OBSERVACIONES)
            );
        }
    
    }
    
}