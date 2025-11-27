/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.AgenteDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.AgenteMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Agente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IAgenteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IAgenteRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

/**
 * Servicio de aplicación para la gestión de Agentes.
 * Implementa la lógica de negocio y orquesta las operaciones CRUD.
 * 
 * <p>Este servicio actúa como intermediario entre la capa de presentación
 * y la capa de persistencia, aplicando validaciones y reglas de negocio.</p>
 * 
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Validar datos de entrada antes de persistir</li>
 *   <li>Convertir entre DTOs y Entidades de dominio</li>
 *   <li>Aplicar reglas de negocio específicas</li>
 *   <li>Manejar excepciones de forma coherente</li>
 *   <li>Garantizar la integridad de los datos</li>
 * </ul>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El número de empleado debe ser único en el sistema</li>
 *   <li>El email debe tener formato válido</li>
 *   <li>El nombre debe tener al menos 3 caracteres</li>
 *   <li>El teléfono debe contener entre 7 y 15 dígitos</li>
 * </ul>
 * 
 * @author Jose 
 * @version 2.0
 * @since 2025
 */
public class AgenteService implements IAgenteService {

    // Expresión regular para validación de email (RFC 5322 simplificada)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Constantes de validación
    private static final int MIN_LONGITUD_NOMBRE = 3;
    private static final int MAX_LONGITUD_NOMBRE = 100;
    private static final int MIN_LONGITUD_NUM_EMPLEADO = 3;
    private static final int MAX_LONGITUD_NUM_EMPLEADO = 20;
    private static final int MIN_DIGITOS_TELEFONO = 7;
    private static final int MAX_DIGITOS_TELEFONO = 15;
    
    private final IAgenteRepository repositorio;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param repositorio implementación del repositorio de agentes
     * @throws IllegalArgumentException si el repositorio es null
     */
    public AgenteService(IAgenteRepository repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException(
                "El repositorio de agentes no puede ser null"
            );
        }
        this.repositorio = repositorio;
    }

    /**
     * Registra un nuevo agente en el sistema.
     * 
     * <p>Realiza las siguientes validaciones antes de persistir:</p>
     * <ul>
     *   <li>DTO no nulo</li>
     *   <li>Nombre completo válido</li>
     *   <li>Número de empleado único</li>
     *   <li>Email con formato correcto</li>
     *   <li>Teléfono con cantidad de dígitos válida</li>
     * </ul>
     * 
     * @param dto objeto de transferencia con los datos del agente
     * @throws IllegalArgumentException si el DTO es null
     * @throws CallCenterException si los datos son inválidos o ya existe un agente con ese número
     */
    @Override
    public void registrarAgente(AgenteDTO dto) {
        // Validación de parámetros
        validarDTONoNulo(dto);
        
        // Convertir DTO a entidad de dominio
        Agente entidad = AgenteMapper.toEntity(dto);
        
        // Aplicar validaciones de negocio
        validarReglasDeNegocio(entidad);
        
        // Validar duplicados (regla de negocio crítica)
        verificarNumeroEmpleadoUnico(entidad.getNumeroEmpleado(), null);
        
        // Persistir
        repositorio.guardar(entidad);
    }

    /**
     * Recupera la lista completa de agentes registrados.
     * 
     * @return lista inmutable de DTOs con información de todos los agentes
     */
    @Override
    public List<AgenteDTO> listarAgentes() {
        return repositorio.listarTodos()
                .stream()
                .map(AgenteMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la información de un agente existente.
     * 
     * <p>Valida que:</p>
     * <ul>
     *   <li>El agente tenga un ID válido</li>
     *   <li>Todos los datos cumplan las reglas de negocio</li>
     *   <li>El número de empleado sea único (excepto el actual)</li>
     * </ul>
     * 
     * @param dto objeto con los datos actualizados (debe incluir ID)
     * @throws IllegalArgumentException si el DTO es null
     * @throws CallCenterException si el ID es null o los datos son inválidos
     */
    @Override
    public void actualizarAgente(AgenteDTO dto) {
        validarDTONoNulo(dto);
        
        if (dto.getId() == null || dto.getId() <= 0) {
            throw new CallCenterException(
                "No se puede actualizar un agente sin un ID válido. " +
                "Asegúrese de seleccionar un agente de la tabla antes de actualizar."
            );
        }
        
        Agente entidad = AgenteMapper.toEntity(dto);
        validarReglasDeNegocio(entidad);
        
        // Validar número de empleado único excluyendo el registro actual
        verificarNumeroEmpleadoUnico(entidad.getNumeroEmpleado(), dto.getId());
        
        repositorio.actualizar(entidad);
    }

    /**
     * Elimina un agente del sistema de forma permanente.
     * 
     * <p><b>ADVERTENCIA:</b> Esta operación no puede deshacerse.
     * Se recomienda implementar soft-delete en producción.</p>
     * 
     * @param id identificador único del agente a eliminar
     * @throws CallCenterException si el ID es inválido
     */
    @Override
    public void eliminarAgente(Long id) {
        if (id == null || id <= 0) {
            throw new CallCenterException(
                "ID de agente inválido para eliminación. Debe ser un número positivo."
            );
        }
        
        // Verificar que el agente existe antes de eliminar
        verificarAgenteExiste(id);
        
        repositorio.eliminar(id);
    }
    
    
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    
    
    /**
     * Valida que el DTO no sea null.
     * 
     * @param dto objeto a validar
     * @throws IllegalArgumentException si el DTO es null
     */
    private void validarDTONoNulo(AgenteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException(
                "El DTO del agente no puede ser null"
            );
        }
    }
    
    /**
     * Aplica todas las reglas de negocio sobre la entidad Agente.
     * 
     * @param agente entidad a validar
     * @throws CallCenterException si alguna validación falla
     */
    private void validarReglasDeNegocio(Agente agente) {
        validarNombreCompleto(agente.getNombreCompleto());
        validarNumeroEmpleado(agente.getNumeroEmpleado());
        validarEmail(agente.getEmail());
        validarTelefono(agente.getTelefonoContacto());
        validarTurno(agente.getHorarioTurno());
        validarNivelExperiencia(agente.getNivelExperiencia());
    }
    
    /**
     * Valida que el nombre completo sea válido.
     * 
     * @param nombre nombre a validar
     * @throws CallCenterException si el nombre es inválido
     */
    private void validarNombreCompleto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new CallCenterException(
                "El nombre del agente es obligatorio"
            );
        }
        
        String nombreLimpio = nombre.trim();
        
        if (nombreLimpio.length() < MIN_LONGITUD_NOMBRE) {
            throw new CallCenterException(
                String.format("El nombre debe tener al menos %d caracteres", MIN_LONGITUD_NOMBRE)
            );
        }
        
        if (nombreLimpio.length() > MAX_LONGITUD_NOMBRE) {
            throw new CallCenterException(
                String.format("El nombre no puede exceder %d caracteres", MAX_LONGITUD_NOMBRE)
            );
        }
        
        // Validar que contenga solo letras y espacios
        if (!nombreLimpio.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new CallCenterException(
                "El nombre solo puede contener letras y espacios"
            );
        }
    }
    
    /**
     * Valida que el número de empleado sea válido.
     * 
     * @param numeroEmpleado número a validar
     * @throws CallCenterException si el número es inválido
     */
    private void validarNumeroEmpleado(String numeroEmpleado) {
        if (numeroEmpleado == null || numeroEmpleado.trim().isEmpty()) {
            throw new CallCenterException(
                "El número de empleado es obligatorio"
            );
        }
        
        String numeroLimpio = numeroEmpleado.trim();
        
        if (numeroLimpio.length() < MIN_LONGITUD_NUM_EMPLEADO || 
            numeroLimpio.length() > MAX_LONGITUD_NUM_EMPLEADO) {
            throw new CallCenterException(
                String.format("El número de empleado debe tener entre %d y %d caracteres",
                    MIN_LONGITUD_NUM_EMPLEADO, MAX_LONGITUD_NUM_EMPLEADO)
            );
        }
        
        // Validar que sea alfanumérico
        if (!numeroLimpio.matches("^[a-zA-Z0-9-]+$")) {
            throw new CallCenterException(
                "El número de empleado solo puede contener letras, números y guiones"
            );
        }
    }
    
    /**
     * Valida el formato del email si se proporciona.
     * 
     * @param email email a validar (puede ser null o vacío)
     * @throws CallCenterException si el formato es inválido
     */
    private void validarEmail(String email) {
        // El email es opcional
        if (email == null || email.trim().isEmpty()) {
            return;
        }
        
        String emailLimpio = email.trim();
        
        if (!EMAIL_PATTERN.matcher(emailLimpio).matches()) {
            throw new CallCenterException(
                "El formato del correo electrónico no es válido. " +
                "Use el formato: usuario@dominio.com"
            );
        }
        
        if (emailLimpio.length() > 100) {
            throw new CallCenterException(
                "El correo electrónico no puede exceder 100 caracteres"
            );
        }
    }
    
    /**
     * Valida el formato del teléfono si se proporciona.
     * 
     * @param telefono teléfono a validar (puede ser null o vacío)
     * @throws CallCenterException si el formato es inválido
     */
    private void validarTelefono(String telefono) {
        // El teléfono es opcional
        if (telefono == null || telefono.trim().isEmpty()) {
            return;
        }
        
        // Remover espacios, guiones, paréntesis y signos +
        String telefonoLimpio = telefono.replaceAll("[\\s\\-\\(\\)\\+]", "");
        
        if (!telefonoLimpio.matches("\\d+")) {
            throw new CallCenterException(
                "El teléfono solo puede contener dígitos, espacios, guiones y paréntesis"
            );
        }
        
        if (telefonoLimpio.length() < MIN_DIGITOS_TELEFONO || 
            telefonoLimpio.length() > MAX_DIGITOS_TELEFONO) {
            throw new CallCenterException(
                String.format("El teléfono debe contener entre %d y %d dígitos",
                    MIN_DIGITOS_TELEFONO, MAX_DIGITOS_TELEFONO)
            );
        }
    }
    
    /**
     * Valida que el turno sea uno de los valores permitidos.
     * 
     * @param turno turno a validar
     * @throws CallCenterException si el turno no es válido
     */
    private void validarTurno(String turno) {
        if (turno == null || turno.trim().isEmpty()) {
            throw new CallCenterException(
                "El horario de turno es obligatorio"
            );
        }
        
        String[] turnosValidos = {"Mañana", "Tarde", "Noche", "Mixto"};
        boolean turnoValido = false;
        
        for (String turnoPermitido : turnosValidos) {
            if (turnoPermitido.equalsIgnoreCase(turno.trim())) {
                turnoValido = true;
                break;
            }
        }
        
        if (!turnoValido) {
            throw new CallCenterException(
                "Turno inválido. Los valores permitidos son: Mañana, Tarde, Noche, Mixto"
            );
        }
    }
    
    /**
     * Valida que el nivel de experiencia sea uno de los valores permitidos.
     * 
     * @param nivelExperiencia nivel a validar
     * @throws CallCenterException si el nivel no es válido
     */
    private void validarNivelExperiencia(String nivelExperiencia) {
        if (nivelExperiencia == null || nivelExperiencia.trim().isEmpty()) {
            throw new CallCenterException(
                "El nivel de experiencia es obligatorio"
            );
        }
        
        String[] nivelesValidos = {"Junior", "Intermedio", "Senior", "Expert"};
        boolean nivelValido = false;
        
        for (String nivel : nivelesValidos) {
            if (nivel.equalsIgnoreCase(nivelExperiencia.trim())) {
                nivelValido = true;
                break;
            }
        }
        
        if (!nivelValido) {
            throw new CallCenterException(
                "Nivel de experiencia inválido. Los valores permitidos son: Junior, Intermedio, Senior, Expert"
            );
        }
    }
    
    /**
     * Verifica que no exista otro agente con el mismo número de empleado.
     * 
     * @param numeroEmpleado número a verificar
     * @param idExcluir ID del agente a excluir de la búsqueda (para actualizaciones)
     * @throws CallCenterException si ya existe otro agente con ese número
     */
    private void verificarNumeroEmpleadoUnico(String numeroEmpleado, Long idExcluir) {
        boolean existe = repositorio.listarTodos()
                .stream()
                .filter(a -> idExcluir == null || !a.getIdAgente().equals(idExcluir))
                .anyMatch(a -> a.getNumeroEmpleado().equalsIgnoreCase(numeroEmpleado.trim()));
        
        if (existe) {
            throw new CallCenterException(
                "Ya existe un agente con el número de empleado: " + numeroEmpleado.trim() + 
                ". El número de empleado debe ser único en el sistema."
            );
        }
    }
    
    /**
     * Verifica que un agente existe antes de operaciones críticas.
     * 
     * @param id identificador del agente
     * @throws CallCenterException si el agente no existe
     */
    private void verificarAgenteExiste(Long id) {
        boolean existe = repositorio.listarTodos()
                .stream()
                .anyMatch(a -> a.getIdAgente().equals(id));
        
        if (!existe) {
            throw new CallCenterException(
                "No se encontró un agente con el ID: " + id
            );
        }
    }
}