package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.CampaniaDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.CampaniaMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Campania;
import com.udc.callcenterdesktop.dominio.puertos.entrada.ICampaniaService;
import com.udc.callcenterdesktop.dominio.puertos.salida.ICampaniaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para la gestión de Campañas.
 * Implementa la lógica de negocio y orquesta las operaciones CRUD.
 * * <p>Una campaña representa una estrategia organizada de ventas,
 * soporte o encuestas que se ejecuta durante un período específico.</p>
 * * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 * <li>El nombre de la campaña debe ser único.</li>
 * <li>La fecha de fin no puede ser anterior a la fecha de inicio.</li>
 * <li>Se requiere al menos un supervisor asignado.</li>
 * <li>Los objetivos deben estar claramente definidos.</li>
 * <li>Las fechas no pueden ser futuras en más de 5 años.</li>
 * </ul>
 * * @author Carlos Molano
 * @version 2.0
 */
public class CampaniaService implements ICampaniaService {

    // CONSTANTES DE VALIDACIÓN - Reglas de Negocio
    private static final int MIN_LONGITUD_NOMBRE = 3;
    private static final int MAX_LONGITUD_NOMBRE = 100;
    private static final int MIN_LONGITUD_OBJETIVOS = 10;
    private static final int MAX_LONGITUD_OBJETIVOS = 500;
    private static final int MAX_ANIOS_FUTUROS = 5;
    
    // Tipos permitidos para asegurar integridad de datos
    private static final String[] TIPOS_VALIDOS = {"Ventas", "Soporte", "Encuesta", "Retención", "Cobranza"};

    private final ICampaniaRepository repositorio;

    /**
     * Constructor con inyección de dependencias.
     * * @param repositorio implementación del repositorio de campañas
     * @throws IllegalArgumentException si el repositorio es null
     */
    public CampaniaService(ICampaniaRepository repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio de campañas no puede ser null");
        }
        this.repositorio = repositorio;
    }

    /**
     * Registra una nueva campaña en el sistema.
     * * @param dto objeto de transferencia con los datos de la campaña
     * @throws CallCenterException si los datos violan alguna regla de negocio
     */
    @Override
    public void registrarCampania(CampaniaDTO dto) {
        // Validar entrada básica
        validarDTONoNulo(dto);
        
        //Convertir a Entidad de Dominio
        Campania entidad = CampaniaMapper.toEntity(dto);
        
        // Validar reglas de negocio
        validarReglasDeNegocio(entidad);
        
        // Validar unicidad (Regla específica de creación)
        verificarNombreUnico(entidad.getNombreCampania(), null);
        
        // Persistir
        repositorio.guardar(entidad);
    }

    /**
     * Recupera la lista completa de campañas registradas.
     * @return lista de DTOs transformados
     */
    @Override
    public List<CampaniaDTO> listarCampanias() {
        return repositorio.listarTodos()
                .stream()
                .map(CampaniaMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la información de una campaña existente.
     * * @param dto objeto con los datos actualizados
     */
    @Override
    public void actualizarCampania(CampaniaDTO dto) {
        validarDTONoNulo(dto);
        
        if (dto.getIdCampania() == null || dto.getIdCampania() <= 0) {
            throw new CallCenterException("ID de campaña requerido para actualizar");
        }
        
        Campania entidad = CampaniaMapper.toEntity(dto);
        validarReglasDeNegocio(entidad);
        
        // Validar unicidad excluyendo la propia campaña que se edita
        verificarNombreUnico(entidad.getNombreCampania(), dto.getIdCampania());
        
        repositorio.actualizar(entidad);
    }

    /**
     * Elimina una campaña del sistema.
     * @param id identificador único
     */
    @Override
    public void eliminarCampania(Long id) {
        if (id == null || id <= 0) {
            throw new CallCenterException("ID nulo o inválido, no se puede eliminar");
        }
        
        verificarCampaniaExiste(id);
        repositorio.eliminar(id);
    }
    
    
    // SECCIÓN DE VALIDACIONES PRIVADAS (Lógica Pura)
   
    private void validarDTONoNulo(CampaniaDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO de la campaña no puede ser null");
        }
    }
    
    private void validarReglasDeNegocio(Campania campania) {
        validarNombreCampania(campania.getNombreCampania());
        validarTipoCampania(campania.getTipoCampania());
        validarFechas(campania.getFechaInicio(), campania.getFechaFin());
        validarSupervisor(campania.getSupervisoresCargo());
        validarObjetivos(campania.getDescripcionObjetivos());
    }
    
    private void validarNombreCampania(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new CallCenterException("El nombre de la campaña es obligatorio");
        }
        String nombreLimpio = nombre.trim();
        if (nombreLimpio.length() < MIN_LONGITUD_NOMBRE) {
            throw new CallCenterException(String.format("El nombre debe tener al menos %d caracteres", MIN_LONGITUD_NOMBRE));
        }
        if (nombreLimpio.length() > MAX_LONGITUD_NOMBRE) {
            throw new CallCenterException(String.format("El nombre no puede exceder %d caracteres", MAX_LONGITUD_NOMBRE));
        }
    }
    
    private void validarTipoCampania(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new CallCenterException("El tipo de campaña es obligatorio");
        }
        boolean tipoValido = false;
        for (String tipoPermitido : TIPOS_VALIDOS) {
            if (tipoPermitido.equalsIgnoreCase(tipo.trim())) {
                tipoValido = true;
                break;
            }
        }
        if (!tipoValido) {
            throw new CallCenterException("Tipo de campaña inválido. Valores permitidos: " + String.join(", ", TIPOS_VALIDOS));
        }
    }
    
    private void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null) {
            throw new CallCenterException("La fecha de inicio es obligatoria");
        }
        
        LocalDate hoy = LocalDate.now();
        LocalDate limiteAniosFuturos = hoy.plusYears(MAX_ANIOS_FUTUROS);
        
        if (fechaInicio.isAfter(limiteAniosFuturos)) {
            throw new CallCenterException("La fecha de inicio no puede ser muy lejana en el futuro");
        }
        
        if (fechaFin != null) {
            if (fechaFin.isBefore(fechaInicio)) {
                throw new CallCenterException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            }
            if (fechaFin.isAfter(limiteAniosFuturos)) {
                throw new CallCenterException("La fecha de fin excede el límite permitido de planificación futura.");
            }
        }
    }
    
    private void validarSupervisor(String supervisor) {
        if (supervisor == null || supervisor.trim().isEmpty()) {
            throw new CallCenterException("Debe asignar al menos un supervisor responsable");
        }
        if (supervisor.trim().length() < 3) {
            throw new CallCenterException("El nombre del supervisor es muy corto");
        }
    }
    
    private void validarObjetivos(String objetivos) {
        if (objetivos == null || objetivos.trim().isEmpty()) {
            throw new CallCenterException("La descripción de objetivos es obligatoria");
        }
        if (objetivos.trim().length() < MIN_LONGITUD_OBJETIVOS) {
            throw new CallCenterException("Describa los objetivos con más detalle (Mínimo " + MIN_LONGITUD_OBJETIVOS + " caracteres)");
        }
        if (objetivos.trim().length() > MAX_LONGITUD_OBJETIVOS) {
            throw new CallCenterException("La descripción es demasiado larga (Máximo " + MAX_LONGITUD_OBJETIVOS + " caracteres)");
        }
    }
    
    /**
     * Verifica duplicados en BD.
     * Nota: En sistemas grandes esto se hace a nivel de BD (Constraint), 
     * pero aquí lo validamos en lógica para dar feedback amigable.
     */
    private void verificarNombreUnico(String nombre, Long idExcluir) {
        boolean existe = repositorio.listarTodos().stream()
                .filter(c -> idExcluir == null || !c.getIdCampania().equals(idExcluir))
                .anyMatch(c -> c.getNombreCampania().equalsIgnoreCase(nombre.trim()));
        
        if (existe) {
            throw new CallCenterException("Ya existe una campaña activa con el nombre: " + nombre.trim());
        }
    }
    
    private void verificarCampaniaExiste(Long id) {
        boolean existe = repositorio.listarTodos().stream()
                .anyMatch(c -> c.getIdCampania().equals(id));
        
        if (!existe) {
            throw new CallCenterException("No se encontró una campaña con el ID: " + id);
        }
    }
}