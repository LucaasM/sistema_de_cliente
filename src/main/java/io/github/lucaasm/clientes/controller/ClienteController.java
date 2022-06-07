package io.github.lucaasm.clientes.controller;

import io.github.lucaasm.clientes.domain.entity.Cliente;
import io.github.lucaasm.clientes.domain.service.ClienteService;
import io.github.lucaasm.clientes.domain.service.ClienteServiceImpl;
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
    public ResponseEntity<?> listarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(clienteService.listarPorid(id));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> adicionarCliente(@Valid @RequestBody Cliente cliente) {
        return new ResponseEntity<>(clienteService.adicionarCliente(cliente), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(
            @Valid
            @PathVariable Long id,
            @RequestBody Cliente clienteAtualizado
    ) {
        try {
            return ResponseEntity.ok().body(clienteService.atualizarCliente(id, clienteAtualizado));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerCliente(@PathVariable Long id) {
        try {
            clienteService.removerCliente(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

}
