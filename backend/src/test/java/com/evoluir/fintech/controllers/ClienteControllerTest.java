package com.evoluir.fintech.controllers;

import com.evoluir.fintech.domain.dtos.ClienteRequestDTO;
import com.evoluir.fintech.domain.dtos.ClienteResponseDTO;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import com.evoluir.fintech.domain.exceptions.CustomExceptionHandler;
import com.evoluir.fintech.services.ClienteService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private ClienteRequestDTO requestDTO;
    private ClienteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // ESSENCIAL para funcionar com @JsonCreator
        objectMapper.findAndRegisterModules();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new CustomExceptionHandler()) // se tiver
                .setMessageConverters(converter)
                .build();

        requestDTO = new ClienteRequestDTO(
                "Lis",
                "12345678900",
                LocalDate.of(1998, 10, 22),
                BigDecimal.valueOf(2000.0)
        );

        responseDTO = new ClienteResponseDTO(
                1L,
                "Lis",
                "12345678900",
                LocalDate.of(1998, 10, 22),
                StatusBloqueio.A,
                BigDecimal.valueOf(2000.0)
        );
    }


    @Test
    void deveCriarClienteComSucesso() throws Exception {
        when(clienteService.create(any())).thenReturn(responseDTO);

        String json = objectMapper.writeValueAsString(requestDTO);
        System.out.println("JSON enviado (criação):\n" + json);

        MvcResult result = mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        System.out.println("Resposta recebida: " + result.getResponse().getContentAsString());

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Lis"))
                .andExpect(jsonPath("$.statusBloqueio").value("A"));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        when(clienteService.findById(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void deveRetornar404SeClienteNaoEncontrado() throws Exception {
        when(clienteService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarClienteComSucesso() throws Exception {
        when(clienteService.update(eq(1L), any())).thenReturn(responseDTO);

        String json = objectMapper.writeValueAsString(requestDTO);
        System.out.println("JSON enviado (atualização):\n" + json);

        MvcResult result = mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        System.out.println("Resposta recebida: " + result.getResponse().getContentAsString());

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lis"));
    }

    @Test
    void deveListarTodosClientes() throws Exception {
        when(clienteService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveListarClientesBloqueados() throws Exception {
        ClienteResponseDTO bloqueado = new ClienteResponseDTO(
                2L, "Ana", "222", LocalDate.now(), StatusBloqueio.B, BigDecimal.ZERO
        );

        when(clienteService.findBlockedClients()).thenReturn(List.of(bloqueado));

        mockMvc.perform(get("/clientes/bloqueados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].statusBloqueio").value("B"));
    }

    @Test
    void testObjectMapperDeserialization() throws Exception {
        String json = """
        {
          "nome": "Lis",
          "cpf": "12345678900",
          "dataNascimento": "1998-10-22",
          "limiteCredito": 2000.0
        }
        """;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.findAndRegisterModules();

        ClienteRequestDTO dto = mapper.readValue(json, ClienteRequestDTO.class);

        assertNotNull(dto.nome());
        assertNotNull(dto.cpf());
        assertNotNull(dto.dataNascimento());
        assertNotNull(dto.limiteCredito());
    }

}
