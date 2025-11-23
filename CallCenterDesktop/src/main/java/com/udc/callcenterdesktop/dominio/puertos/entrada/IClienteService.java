/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.udc.callcenterdesktop.dominio.puertos.entrada;

/**
 *
 * @author Admin
 */
import com.udc.callcenterdesktop.aplicacion.dto.ClienteDTO;
import java.util.List;

/**
 * Puerto de Entrada (Input Port).
 * Define las operaciones disponibles para la gestión de clientes
 * que pueden ser consumidas por la interfaz gráfica.
 */
public interface IClienteService {
    void registrarCliente(ClienteDTO dto);
    List<ClienteDTO> listarClientes();
    void actualizarCliente(ClienteDTO dto);
    void eliminarCliente(Long id);
}