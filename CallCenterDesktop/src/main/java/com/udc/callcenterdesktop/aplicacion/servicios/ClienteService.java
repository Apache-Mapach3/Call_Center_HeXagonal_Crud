/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.aplicacion.servicios;

import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import com.udc.callcenterdesktop.aplicacion.mapper.ClienteMapper;
import com.udc.callcenterdesktop.dominio.excepciones.CallCenterException;
import com.udc.callcenterdesktop.dominio.modelo.Cliente;
import com.udc.callcenterdesktop.dominio.puertos.entrada.IClienteService;
import com.udc.callcenterdesktop.dominio.puertos.salida.IClienteRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

/**
 * Servicio de aplicación para la gestión de Clientes.
 * Implementa la lógica de negocio y orquesta las operaciones CRUD.
 * 
 * <p>Este servicio coordina las operaciones relacionadas con clientes,
 * aplicando validaciones estrictas y garantizando la integridad de los datos.</p>
 * 
 * <p><b>Reglas de negocio implementadas:</b></p>
 * <ul>
 *   <li>El documento de identidad debe ser único en el sistema</li>
 *   <li>Al menos un método de contacto (teléfono o email) es requerido</li>
 *   <li>El nombre completo debe tener al menos 3 caracteres</li>
 *   <li>El email debe tener formato válido RFC 5322</li>
 * </ul>
 * 
 * @author Carlos 
 * @version 2.0
 * @since 2025
 */
public class ClienteService implements IClienteService {

    // Patrón de validación para emails
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Constantes de validación
    private static final int MIN_LONGITUD_NOMBRE = 3;
    private static final int MAX_LONGITUD_NOMBRE = 100;
    private static final int MIN_LONGITUD_DOCUMENTO = 5;
    private static final int MAX_LONGITUD_DOCUMENTO = 20;
    private static final int MIN_DIGITOS_TELEFONO = 7;
    private static final int MAX_DIGITOS_TELEFONO = 15;
    
    private final IClienteRepository repositorio;

    /**
     * Constructor con inyección de dependencias.
     * 
     * @param repositorio implementación del repositorio de clientes
     * @throws IllegalArgumentException si el repositorio es null
     */
    public ClienteService(IClienteRepository repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException(
                "El repositorio de clientes no puede ser null"
            );
        }
        this.repositorio = repositorio;
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * 
     * <p>Validaciones realizadas:</p>
     * <ul>
     *   <li>Nombre completo válido</li>
     *   <li>Documento de identidad único</li>
     *   <li>Al menos un método de contacto</li>
     *   <li>Formato de email correcto</li>
     * </ul>
     * 
     * @param dto objeto de transferencia con los datos del cliente
     * @throws IllegalArgumentException si el DTO es null
     * @throws CallCenterException si los datos son inválidos
     */
    @Override
    public void registrarCliente(ClienteDTO dto) {
        validarDTONoNulo(dto);
        
        // Convertir y validar
        Cliente entidad = ClienteMapper.toEntity(dto);
        validarReglasDeNegocio(entidad);
        
        // Validar documento único
        verificarDocumentoUnico(entidad.getDocumentoIdentidad(), null);
        
        // Persistir
        repositorio.guardar(entidad);
    }

    /**
     * Recupera la lista completa de clientes registrados.
     * 
     * @return lista de DTOs con información de todos los clientes
     */
    @Override
    public List<ClienteDTO> listarClientes() {
        return repositorio.listarTodos()
                .stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza la información de un cliente existente.
     * 
     * @param dto objeto con los datos actualizados (debe incluir ID)
     * @throws IllegalArgumentException si el DTO es null
     * @throws CallCenterException si el ID es null o los datos son inválidos
     */
    @Override
    public void actualizarCliente(ClienteDTO dto) {
        validarDTONoNulo(dto);
        
        if (dto.getIdCliente() == null || dto.getIdCliente() <= 0) {
            throw new CallCenterException(
                "No se puede actualizar un cliente sin un ID válido"
            );
        }
        
        Cliente entidad = ClienteMapper.toEntity(dto);
        validarReglasDeNegocio(entidad);
        
        // Validar documento único excluyendo el registro actual
        verificarDocumentoUnico(entidad.getDocumentoIdentidad(), dto.getIdCliente());
        
        repositorio.actualizar(entidad);
    }

    /**
     * Elimina un cliente del sistema de forma permanente.
     * 
     * @param id identificador único del cliente
     * @throws CallCenterException si el ID es inválido
     */
    @Override
    public void eliminarCliente(Long id) {
        if (id == null || id <= 0) {
            throw new CallCenterException(
                "ID inválido para eliminación"
            );
        }
        
        verificarClienteExiste(id);
        repositorio.eliminar(id);
    }
    
    
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    
    
    private void validarDTONoNulo(ClienteDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO del cliente no puede ser null");
        }
    }
    
    private void validarReglasDeNegocio(Cliente cliente) {
        validarNombreCompleto(cliente.getNombreCompleto());
        validarDocumentoIdentidad(cliente.getDocumentoIdentidad());
        validarMetodoContacto(cliente.getTelefono(), cliente.getEmail());
        validarEmail(cliente.getEmail());
        validarTelefono(cliente.getTelefono());
    }
    
    private void validarNombreCompleto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new CallCenterException("El nombre del cliente es obligatorio");
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
    }
    
    private void validarDocumentoIdentidad(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new CallCenterException("El documento de identidad es obligatorio");
        }
        
        String docLimpio = documento.trim();
        
        if (docLimpio.length() < MIN_LONGITUD_DOCUMENTO || 
            docLimpio.length() > MAX_LONGITUD_DOCUMENTO) {
            throw new CallCenterException(
                String.format("El documento debe tener entre %d y %d caracteres",
                    MIN_LONGITUD_DOCUMENTO, MAX_LONGITUD_DOCUMENTO)
            );
        }
    }
    
    private void validarMetodoContacto(String telefono, String email) {
        boolean tieneTelefono = telefono != null && !telefono.trim().isEmpty();
        boolean tieneEmail = email != null && !email.trim().isEmpty();
        
        if (!tieneTelefono && !tieneEmail) {
            throw new CallCenterException(
                "Debe proporcionar al menos un método de contacto (teléfono o email)"
            );
        }
    }
    
    private void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return; // Email opcional si hay teléfono
        }
        
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new CallCenterException(
                "El formato del email no es válido. Use el formato: usuario@dominio.com"
            );
        }
    }
    
    private void validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return; // Teléfono opcional si hay email
        }
        
        String telefonoLimpio = telefono.replaceAll("[\\s\\-\\(\\)\\+]", "");
        
        if (!telefonoLimpio.matches("\\d+")) {
            throw new CallCenterException(
                "El teléfono solo puede contener dígitos y caracteres de formato"
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
    
    private void verificarDocumentoUnico(String documento, Long idExcluir) {
        boolean existe = repositorio.listarTodos()
                .stream()
                .filter(c -> idExcluir == null || !c.getIdCliente().equals(idExcluir))
                .anyMatch(c -> c.getDocumentoIdentidad().equalsIgnoreCase(documento.trim()));
        
        if (existe) {
            throw new CallCenterException(
                "Ya existe un cliente registrado con el documento: " + documento.trim()
            );
        }
    }
    
    private void verificarClienteExiste(Long id) {
        boolean existe = repositorio.listarTodos()
                .stream()
                .anyMatch(c -> c.getIdCliente().equals(id));
        
        if (!existe) {
            throw new CallCenterException(
                "No se encontró un cliente con el ID: " + id
            );
        }
    }
}