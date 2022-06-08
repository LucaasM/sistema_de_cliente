package io.github.lucaasm.clientes.domain.repository;

import io.github.lucaasm.clientes.domain.entity.Cliente;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ClienteRepositoryTests {

    @Autowired
    ClienteRepository repository;

    @Autowired
    TestEntityManager entityManager;

    Cliente newCliente;

    @BeforeEach
    public void setUp(){
        this.newCliente = Cliente.builder().nome("Lucas Martins").cpf("70110791495").build();
    }

    @Test
    @DisplayName("Deve retornar um cliente ao tentar cadastrar cliente existente")
    public void createClienteExistente(){

        // Cenário
        entityManager.persist(newCliente);

        // Execucao
        Cliente clientePersistido = repository.findByCpf(newCliente.getCpf());

        // Verificacao
        Assertions.assertThat(clientePersistido).isNotNull();
    }

    @Test
    @DisplayName("Deve retornar um novo cliente ao cadastrar um cliente")
    public void createClienteTest(){

        // Cenario e Execucao
        Cliente clientePersistido = entityManager.persist(newCliente);

        // Verificacao
        Assertions.assertThat(clientePersistido).isNotNull();
        Assertions.assertThat(clientePersistido.getId()).isNotNull();
    }
}
