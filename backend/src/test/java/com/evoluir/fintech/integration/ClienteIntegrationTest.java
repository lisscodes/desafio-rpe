package com.evoluir.fintech.integration;

import com.evoluir.fintech.domain.entities.Cliente;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import com.evoluir.fintech.repositories.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClienteIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void deveSalvarERecuperarCliente() {
        Cliente cliente = new Cliente(
                "Cliente Integração",
                "98765432100",
                LocalDate.of(2000, 1, 1),
                StatusBloqueio.A,
                1500.0
        );

        Cliente salvo = clienteRepository.save(cliente);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getCpf()).isEqualTo("98765432100");

        Cliente encontrado = clienteRepository.findById(salvo.getId()).orElseThrow();
        assertThat(encontrado.getNome()).isEqualTo("Cliente Integração");
    }
}
