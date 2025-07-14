package com.evoluir.fintech.domain.dtos;

import com.evoluir.fintech.domain.entities.StatusBloqueio;
import java.time.LocalDate;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        StatusBloqueio statusBloqueio,
        Double limiteCredito
) {}
