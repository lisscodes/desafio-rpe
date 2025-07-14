package com.evoluir.fintech.domain.dtos;

import com.evoluir.fintech.domain.entities.StatusFatura;
import java.math.BigDecimal;
import java.time.LocalDate;

public record FaturaResponseDTO(
        Long id,
        Long clienteId,
        LocalDate dataVencimento,
        LocalDate dataPagamento,
        BigDecimal valor,
        StatusFatura status
) {}
