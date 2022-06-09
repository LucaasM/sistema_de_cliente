package io.github.lucaasm.clientes.domain.service;

import io.github.lucaasm.clientes.controller.exception.ClienteCpfExisteException;
import io.github.lucaasm.clientes.controller.exception.NotFindByIdExcepetion;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ClienteServiceTests {

    ClienteService service;
    Cliente cliente;
    Cliente clienteSalvo;

    @MockBean
    ClienteRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new ClienteServiceImpl(repository);
        this.cliente = Cliente.builder().nome("Lucas Martins").cpf("70110791495").build();
        this.clienteSalvo =  Cliente.builder().id(1l).nome("Lucas Martins").cpf("70110791495")
                .dataCadastro(LocalDate.now()).build();
    }

    @Test
    @DisplayName("Deve lançar ao cadastrar um cliente valido")
    public void criandoClienteTest(){

        Cliente clientePersistido = Cliente.builder().id(1L).nome("Lucas Martins").cpf("70110791495")
                .dataCadastro(LocalDate.now()).build();
        Mockito.when( repository.save(cliente)).thenReturn(clientePersistido);

        assertThat(clientePersistido.getId()).isNotNull();
        assertThat(clientePersistido.getNome()).isEqualTo(cliente.getNome());
        assertThat(clientePersistido.getCpf()).isEqualTo(cliente.getCpf());
        assertThat(clientePersistido.getDataCadastro()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Deve lançar ao tentar salvar um cliente com um CPF existente")
    public void criandoClienteComCpfExistenteTest(){

        Mockito.when( repository.findByCpf(Mockito.anyString()) ).thenReturn(cliente);

        Throwable exception = catchThrowable(() -> service.adicionarCliente(cliente));

        assertThat(exception)
                .isInstanceOf(ClienteCpfExisteException.class)
                .hasMessage("Já existe um cliente com o CPF 70110791495");
    }

    @Test
    @DisplayName("Deve lançar ao tentar listar um cliente com ID inexistente")
    public void buscarClienteInexistente() throws NotFindByIdExcepetion {

        Long id = 1L;
        Mockito.when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());


        Throwable exception =  catchThrowable(() -> service.listarPorid(id));

        assertThat(exception)
                .isInstanceOf(NotFindByIdExcepetion.class)
                .hasMessage(String.format("Não existe nenhum cliente com o código %d", id));
    }

    @Test
    @DisplayName("Deve lançar ao listar um cliente existente")
    public void buscarClientePorIdExistente() throws NotFindByIdExcepetion {


        Mockito.when( repository.findById(Mockito.anyLong())).thenReturn(Optional.of(clienteSalvo));

        Cliente clientePersistido = service.listarPorid(1L);

        assertThat(clientePersistido);
        assertThat(clientePersistido.getId()).isEqualTo(clienteSalvo.getId());
        assertThat(clientePersistido.getNome()).isEqualTo(clienteSalvo.getNome());
        assertThat(clientePersistido.getCpf()).isEqualTo(clienteSalvo.getCpf());
        assertThat(clientePersistido.getDataCadastro())
                .isEqualTo(clienteSalvo.getDataCadastro());

    }

    @Test
    @DisplayName("Deve lançar ao tentar atualizar cliente inexistente")
    public void atualizarClienteInexistente(){

        Long id = 1l;
        Mockito.when( repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> service.atualizarCliente(id, cliente));

        assertThat(exception)
                .isInstanceOf(NotFindByIdExcepetion.class)
                .hasMessage(String.format("Não existe nenhum cliente com o código %d", id));

    }

    @Test
    @DisplayName("Deve lançar ao atualizar um cliente existente")
    public void atualizarClienteExistente() throws Exception {

        Long id = 1l;

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(clienteSalvo));

        Mockito.when(repository.save(clienteSalvo)).thenReturn(clienteSalvo);

        Cliente clientePersistido = service.atualizarCliente(1L, cliente);

        assertThat(clientePersistido);
        assertThat(clientePersistido.getId()).isEqualTo(clienteSalvo.getId());
        assertThat(clientePersistido.getNome()).isEqualTo(clienteSalvo.getNome());
        assertThat(clientePersistido.getCpf()).isEqualTo(clienteSalvo.getCpf());
        assertThat(clientePersistido.getDataCadastro())
                .isEqualTo(clienteSalvo.getDataCadastro());
    }

    @Test
    @DisplayName("Deve lançar ao tentar remover um cliente inexistente")
    public void deletarClienteInexistente() throws Exception{

        Long id = 1l;

        Mockito.when( repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> service.removerCliente(id));

        assertThat(exception)
                .isInstanceOf(NotFindByIdExcepetion.class)
                .hasMessage(String.format("Não existe nenhum cliente com o código %d", id));

    }

    @Test
    @DisplayName("Deve lançar ao ao remover um cliente existente")
    public void deletarClienteExistente() throws Exception{

        Long id = 1l;

        Mockito.when( repository.findById(Mockito.anyLong())).thenReturn(Optional.of(clienteSalvo));

        String clientePersitido = service.removerCliente(id);

        assertThat(clientePersitido);
        assertThat(clientePersitido).isNull();

    }
}
