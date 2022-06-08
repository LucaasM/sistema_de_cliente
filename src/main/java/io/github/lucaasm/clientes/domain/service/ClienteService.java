package io.github.lucaasm.clientes.domain.service;

import io.github.lucaasm.clientes.controller.exception.ClienteCpfExisteException;
import io.github.lucaasm.clientes.controller.exception.NotFindByIdExcepetion;
import io.github.lucaasm.clientes.domain.entity.Cliente;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public interface ClienteService {

    List<Cliente> listarClientes();
    Cliente adicionarCliente(Cliente cliente) throws ClienteCpfExisteException;
    Cliente listarPorid(Long id) throws NotFindByIdExcepetion;
    Cliente atualizarCliente(Long id, Cliente clienteAtualizado) throws NotFindByIdExcepetion;
    String removerCliente(Long id) throws NotFindByIdExcepetion;
}
