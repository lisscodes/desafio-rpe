package com.evoluir.fintech.controllers;

import com.evoluir.fintech.domain.dtos.FaturaResponseDTO;
import com.evoluir.fintech.domain.entities.StatusFatura;
import com.evoluir.fintech.services.FaturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(FaturaController.class)
@Import(FaturaControllerTest.Config.class)
class FaturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FaturaService faturaService;

    @Autowired
    private ObjectMapper objectMapper;

    private FaturaResponseDTO faturaDTO;

    @TestConfiguration
    static class Config {
        @Bean
        public FaturaService faturaService() {
            return mock(FaturaService.class);
        }
    }

    @BeforeEach
    void setUp() {
        faturaDTO = new FaturaResponseDTO(
                1L,
                1L,
                LocalDate.of(2024, 7, 15),
                null,
                new BigDecimal("150.00"),
                StatusFatura.B
        );
    }

    @Test
    void deveListarFaturasDeUmCliente() throws Exception {
        when(faturaService.findByClienteId(1L)).thenReturn(List.of(faturaDTO));

        mockMvc.perform(get("/faturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].clienteId").value(1L));
    }

    @Test
    void deveRegistrarPagamentoDaFatura() throws Exception {
        FaturaResponseDTO faturaPaga = new FaturaResponseDTO(
                1L, 1L, LocalDate.of(2024, 7, 15), LocalDate.now(), BigDecimal.valueOf(150.00), StatusFatura.P);
        when(faturaService.registrarPagamento(1L)).thenReturn(faturaPaga);
        mockMvc.perform(put("/faturas/1/pagamento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("P"))
                .andExpect(jsonPath("$.dataPagamento").isNotEmpty());
    }

    @Test
    void deveListarFaturasEmAtraso() throws Exception {
        when(faturaService.findFaturasAtrasadas()).thenReturn(List.of(faturaDTO));

        mockMvc.perform(get("/faturas/atrasadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("B"));
    }

    @Test
    void deveRetornar404SePagamentoNaoEncontrarFatura() throws Exception {
        when(faturaService.registrarPagamento(99L)).thenThrow(new RuntimeException("Fatura não encontrada"));

        mockMvc.perform(put("/faturas/99/pagamento"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400ParaFaturaRequestInvalida() throws Exception {
        String jsonInvalido = """
        {
          "clienteId": null,
          "dataVencimento": "2025-08-15",
          "valor": -150.00
        }
        """;

        mockMvc.perform(post("/faturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Error"));
    }

    @Test
    void deveRetornar400ParaJsonMalformado() throws Exception {
        String jsonMalformado = "{ \"clienteId\": 1, \"dataVencimento\": \"2025-08-15\", \"valor\": ";

        mockMvc.perform(post("/faturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMalformado))
                .andExpect(status().isNotFound());
    }
}
