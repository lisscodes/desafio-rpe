package com.evoluir.fintech.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "clientes")
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_bloqueio", nullable = false)
    private StatusBloqueio statusBloqueio = StatusBloqueio.A;

    @Column(name = "limite_credito")
    private BigDecimal limiteCredito;

    public Cliente(String nome, String cpf, LocalDate dataNascimento, StatusBloqueio statusBloqueio, BigDecimal limiteCredito) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.statusBloqueio = statusBloqueio;
        this.limiteCredito = limiteCredito;
    }

}
