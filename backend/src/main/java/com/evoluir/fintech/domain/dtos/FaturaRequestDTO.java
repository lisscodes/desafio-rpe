package com.evoluir.fintech.domain.dtos;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FaturaRequestDTO(
        @NotNull Long clienteId,
        @NotNull LocalDate dataVencimento,
        @NotNull BigDecimal valor
) {}
