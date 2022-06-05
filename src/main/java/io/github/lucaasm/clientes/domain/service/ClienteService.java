package io.github.lucaasm.clientes.domain.service;

import io.github.lucaasm.clientes.domain.entity.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClienteService {

    List<Cliente> listarClientes();
    Cliente adicionarCliente(Cliente cliente);
    Cliente listarPorid(Long id) throws Exception;
    Cliente atualizarCliente(Long id, Cliente clienteAtualizado) throws Exception;
    void removerCliente(Long id) throws Exception;
}
