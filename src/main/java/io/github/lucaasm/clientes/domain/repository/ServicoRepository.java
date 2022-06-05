package io.github.lucaasm.clientes.domain.repository;

import io.github.lucaasm.clientes.domain.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
