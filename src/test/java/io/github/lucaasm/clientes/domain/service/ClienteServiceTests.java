package io.github.lucaasm.clientes.domain.service;

import io.github.lucaasm.clientes.controller.exception.ClienteCpfExisteException;
import io.github.lucaasm.clientes.domain.entity.Cliente;
import io.github.lucaasm.clientes.domain.repository.ClienteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClienteServiceTests {

    ClienteService service;
    Cliente cliente;

    @MockBean
    ClienteRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new ClienteServiceImpl(repository);
        this.cliente = Cliente.builder().nome("Lucas Martins").cpf("70110791495").build();
    }

    @Test
    @DisplayName("Deve lançar ao cadastrar um cliente valido")
    public void createClienteTests(){

        Cliente clientePersistido = Cliente.builder().id(1L).nome("Lucas Martins").cpf("70110791495")
                .dataCadastro(LocalDate.now()).build();
        Mockito.when( repository.save(cliente)).thenReturn(clientePersistido);

        Assertions.assertThat(clientePersistido.getId()).isNotNull();
        Assertions.assertThat(clientePersistido.getNome()).isEqualTo(cliente.getNome());
        Assertions.assertThat(clientePersistido.getCpf()).isEqualTo(cliente.getCpf());
        Assertions.assertThat(clientePersistido.getDataCadastro()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Deve lançar ao tentar salva um cliente com um CPF existente")
    public void createClienteCpfExistsTest(){

        Mockito.when( repository.findByCpf(Mockito.anyString()) ).thenReturn(cliente);

        Throwable exception = Assertions.catchThrowable(() -> service.adicionarCliente(cliente));

        Assertions
                .assertThat(exception)
                .isInstanceOf(ClienteCpfExisteException.class)
                .hasMessage("Já existe um cliente com o CPF 70110791495");
    }
}
