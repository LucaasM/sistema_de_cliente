package io.github.lucaasm.clientes.domain.repository;

import io.github.lucaasm.clientes.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
