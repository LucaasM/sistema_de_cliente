package io.github.lucaasm.clientes.controller;

import io.github.lucaasm.clientes.controller.exception.ClienteCpfExisteException;
import io.github.lucaasm.clientes.controller.exception.NotFindByIdExcepetion;
import io.github.lucaasm.clientes.domain.entity.Cliente;
import io.github.lucaasm.clientes.domain.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarCliente() {
        return ResponseEntity.ok().body(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarPorId(@PathVariable Long id) throws NotFindByIdExcepetion {
            return ResponseEntity.ok().body(clienteService.listarPorid(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> adicionarCliente(@Valid @RequestBody Cliente cliente) throws ClienteCpfExisteException {
        return new ResponseEntity<>(clienteService.adicionarCliente(cliente), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(
            @Valid
            @PathVariable Long id,
            @RequestBody Cliente clienteAtualizado
    ) throws NotFindByIdExcepetion {
            return ResponseEntity.ok().body(clienteService.atualizarCliente(id, clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerCliente(@PathVariable Long id) throws NotFindByIdExcepetion {
            clienteService.removerCliente(id);
            return ResponseEntity.noContent().build();
    }

}
