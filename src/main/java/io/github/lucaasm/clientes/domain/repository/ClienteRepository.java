package io.github.lucaasm.clientes.domain.repository;

import ch.qos.logback.core.net.server.Client;
import io.github.lucaasm.clientes.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findByCpf(String cpf);
}
