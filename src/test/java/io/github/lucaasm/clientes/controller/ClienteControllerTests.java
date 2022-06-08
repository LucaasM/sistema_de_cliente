package io.github.lucaasm.clientes.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lucaasm.clientes.controller.exception.NotFindByIdExcepetion;
import io.github.lucaasm.clientes.domain.entity.Cliente;
import io.github.lucaasm.clientes.domain.service.ClienteService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class ClienteControllerTests {

    static String CLIENTE_API = "/cliente";


    @MockBean
    ClienteService service;

    Cliente clienteRequest;
    Cliente clienteResponse;


    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void setUp(){
        this.clienteRequest = Cliente.builder().nome("Lucas Martins").cpf("70110791495").build();
        this.clienteResponse = Cliente.builder().id(1l).nome("Lucas Martins").cpf("70110791495")
                .dataCadastro(LocalDate.now()).build();

    }

    @Test
    @DisplayName("Deve lançar ao criar cliente no sistema")
    public void criandoCliente() throws Exception{

        Cliente savedCliente = Cliente.builder().id(1L).nome("Lucas Martins").cpf("70110791495").dataCadastro(LocalDate.now()).build();

        String json = new ObjectMapper().writeValueAsString(clienteRequest);

        // Dado a execução do meu service.adicinarCliente enviando qualquer livro como parametro,
        // ele retornará um savedCliente
        BDDMockito.given(service.adicionarCliente(Mockito.any(Cliente.class))).willReturn(savedCliente);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(clienteRequest.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(clienteRequest.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("dataCadastro")
                        .value(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar criar um cliente com dados vazios")
    public void criandoClienteSemDados() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new Cliente());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect( MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Deve lançar ao tentar criar um cliente sem nome")
    public void criandoClienteSemNome() throws Exception{

        String json = new ObjectMapper().writeValueAsString(Cliente.builder().cpf("70110791495").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]")
                        .value("O campo NOME e obrigatorio."));
    }

    @Test
    @DisplayName("Deve lançar ao tentar criar um cliente com CPF inválido")
    public void criandoClienteComCpfInvalido() throws Exception{

        String json = new ObjectMapper()
                .writeValueAsString(Cliente.builder().nome("Lucas Martins").cpf("70110391495").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]")
                        .value("CPF esta invalido."));
    }

    @Test
    @DisplayName("Deve lançar ao tentar criar um cliente sem CPF")
    public void criandoClienteSemCpf() throws Exception{

        String json = new ObjectMapper()
                .writeValueAsString(Cliente.builder().nome("Lucas Martins").build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CLIENTE_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]")
                        .value("O campo CPF e obrigatorio."));
    }

    @Test
    @DisplayName("Deve retornar as informações do cliente")
    public void clienteExistente() throws Exception {
        // Cenario (given)

        Long id = 1l;

        BDDMockito.given(service.listarPorid(id)).willReturn(clienteResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(CLIENTE_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(clienteResponse.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(clienteResponse.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("dataCadastro")
                        .value(clienteResponse.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        System.out.println(clienteResponse);

    }

    @Test
    @DisplayName("Deve um erro ao tentar pesqusiar por um cliente inexistente")
    public void clienteNãoExistente() throws Exception {
        // Cenario (given)

        Long id = 2l;

        BDDMockito.given(service.listarPorid(id))
                .willThrow(new NotFindByIdExcepetion(String.format("Não existe nenhum cliente com o código %d", id)));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(CLIENTE_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(
                        String.format("Não existe nenhum cliente com o código %d", id)
                ));

    }


    @Test
    @DisplayName("Deve lançar ao tentar atualizar um cliente inexistente")
    public void clienteNãoExisteAoAtualizar() throws Exception {

        Long id = 2l;

        clienteRequest.setId(1L);
        BDDMockito.given(service.atualizarCliente(id, clienteRequest))
                .willThrow(new NotFindByIdExcepetion(String.format("Não existe nenhum cliente com o código %d", id)));

        String json = new ObjectMapper().writeValueAsString(clienteRequest);


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(CLIENTE_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(
                        String.format("Não existe nenhum cliente com o código %d", id)
                ));
    }

    @Test
    @DisplayName("Deve lançar ao atualizar um cliente existente")
    public void atualizarClienteExistente() throws Exception {

        Long id = 1l;

        clienteRequest.setId(1L);
        BDDMockito.given(service.atualizarCliente(id, clienteRequest))
                .willReturn(clienteResponse);

        String json = new ObjectMapper().writeValueAsString(clienteRequest);


        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(CLIENTE_API.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(clienteResponse.getNome()))
                .andExpect(MockMvcResultMatchers.jsonPath("cpf").value(clienteResponse.getCpf()))
                .andExpect(MockMvcResultMatchers.jsonPath("dataCadastro")
                        .value(clienteResponse.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
    }

    @Test
    @DisplayName("Deve lançar ao tentar excluir um cliente que não existe")
    public void deletarClienteInexistente() throws Exception {

        Long id = 1l;

        BDDMockito.given(service.removerCliente(id))
                .willThrow(new NotFindByIdExcepetion(String.format("Não existe nenhum cliente com o codigo %d", id)));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(CLIENTE_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(
                        String.format("Não existe nenhum cliente com o codigo %d", id)
                ));

    }

    @Test
    @DisplayName("Deve lançar ao deletar um cliente existente")
    public void deletarClienteExistente() throws Exception {


        BDDMockito.given(service.removerCliente(Mockito.anyLong()))
                .willReturn("");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(CLIENTE_API.concat("/" + 1l))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

}
