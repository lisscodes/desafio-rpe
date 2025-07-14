package com.evoluir.fintech.domain.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public class ClienteRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private final String nome;

    @NotBlank(message = "CPF é obrigatório")
    private final String cpf;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate dataNascimento;

    @NotNull(message = "Limite de crédito é obrigatório")
    private final Double limiteCredito;

    @JsonCreator
    public ClienteRequestDTO(
            @JsonProperty("nome") String nome,
            @JsonProperty("cpf") String cpf,
            @JsonProperty("dataNascimento") LocalDate dataNascimento,
            @JsonProperty("limiteCredito") Double limiteCredito
    ) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.limiteCredito = limiteCredito;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public Double getLimiteCredito() {
        return limiteCredito;
    }
}
