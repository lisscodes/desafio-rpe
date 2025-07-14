package com.evoluir.fintech.domain.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteRequestDTO(@NotBlank(message = "Nome é obrigatório") String nome,
                                @NotBlank(message = "CPF é obrigatório") @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos") String cpf,
                                @JsonFormat(pattern = "yyyy-MM-dd") @NotNull(message = "Data de nascimento é obrigatória") @Past(message = "Data de nascimento deve ser no passado") LocalDate dataNascimento,
                                @NotNull(message = "Limite de crédito é obrigatório") @PositiveOrZero(message = "Limite de crédito deve ser maior ou igual a zero") BigDecimal limiteCredito) {
}