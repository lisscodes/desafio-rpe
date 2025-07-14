package com.evoluir.fintech.domain.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FaturaRequestDTO(
        @NotNull(message = "ID do cliente é obrigatório")
        Long clienteId,
        @NotNull(message = "Data de vencimento é obrigatória")
        LocalDate dataVencimento,
        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal valor
) {
}