package io.github.lucaasm.clientes.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.lucaasm.clientes.controller.exception.ApiErrors;
import io.github.lucaasm.clientes.domain.entity.Cliente;
import io.github.lucaasm.clientes.domain.service.ClienteService;
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
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class ClienteControllerTests {

    static String CLIENTE_API = "/cliente";

    @MockBean
    ClienteService service;

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Criando cliente no sistema")
    public void createCliente() throws Exception{

        Cliente clienteRequest = Cliente.builder().id(1L).nome("Lucas Martins").cpf("70110791495").build();
        Cliente savedCliente = Cliente.builder().id(1L).nome("Lucas Martins").cpf("70110791495").dataCadastro(LocalDate.now()).build();

        String json = new ObjectMapper().writeValueAsString(clienteRequest);

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

}
