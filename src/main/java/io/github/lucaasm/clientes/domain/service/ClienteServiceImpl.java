package io.github.lucaasm.clientes.domain.service;

import io.github.lucaasm.clientes.controller.exception.ClienteCpfExisteException;
import io.github.lucaasm.clientes.controller.exception.NotFindByIdExcepetion;
import io.github.lucaasm.clientes.domain.entity.Cliente;
import io.github.lucaasm.clientes.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente listarPorid (Long id) throws NotFindByIdExcepetion {
        return clienteRepository.findById(id).orElseThrow(() -> new NotFindByIdExcepetion(
                String.format("Não existe nenhum cliente com o código %d", id)
        ));
    }

    public Cliente adicionarCliente(Cliente cliente) throws ClienteCpfExisteException {
        Cliente clientePersistido = clienteRepository.findByCpf(cliente.getCpf());
        if(clientePersistido != null) {
            throw new ClienteCpfExisteException(String.format("Já existe um cliente com o CPF %s", cliente.getCpf()));
        }
        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) throws NotFindByIdExcepetion {
        Cliente clientePersistido = clienteRepository.findById(id).orElseThrow(() -> new NotFindByIdExcepetion(
                String.format("Não existe nenhum cliente com o código %d", id)
        ));

        clienteAtualizado.setId(clientePersistido.getId());
        return clienteRepository.save(clienteAtualizado);
    }

    public String removerCliente(Long id) throws NotFindByIdExcepetion {
        Cliente clientePersistido = clienteRepository.findById(id).orElseThrow(() -> new NotFindByIdExcepetion(
                String.format("Não existe nenhum cliente com o código %d", id)
        ));

        clienteRepository.delete(clientePersistido);
        return null;
    }

}
