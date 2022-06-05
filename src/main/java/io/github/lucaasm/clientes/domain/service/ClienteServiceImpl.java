package io.github.lucaasm.clientes.domain.service;

import io.github.lucaasm.clientes.domain.entity.Cliente;
import io.github.lucaasm.clientes.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente listarPorid (Long id) throws Exception {
        return clienteRepository.findById(id).orElseThrow(() -> new Exception(
                String.format("Não existe nenhum cliente com o código %d", id)
        ));
    }

    public Cliente adicionarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) throws Exception {
        Cliente clientePersistido = clienteRepository.findById(id).orElseThrow(() -> new Exception(
                String.format("Não existe nenhum cliente com o código %d", id)
        ));

        clienteAtualizado.setId(clientePersistido.getId());

        return clienteRepository.save(clienteAtualizado);
    }

    public void removerCliente(Long id) throws Exception {
        Cliente clientePersistido = clienteRepository.findById(id).orElseThrow(() -> new Exception(
                String.format("Não existe nenhum cliente com o código %d", id)
        ));

        clienteRepository.delete(clientePersistido);
    }

}
