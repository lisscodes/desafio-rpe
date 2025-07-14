package com.evoluir.fintech.integration;

import com.evoluir.fintech.domain.dtos.FaturaResponseDTO;
import com.evoluir.fintech.domain.entities.Cliente;
import com.evoluir.fintech.domain.entities.Fatura;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import com.evoluir.fintech.domain.entities.StatusFatura;
import com.evoluir.fintech.repositories.ClienteRepository;
import com.evoluir.fintech.repositories.FaturaRepository;
import com.evoluir.fintech.services.FaturaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ClienteIntegrationTest {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private FaturaRepository faturaRepository;
    @Autowired
    private FaturaService faturaService;

    private List<Long> clienteIdToDelete = new ArrayList<>();

    @Test
    void deveSalvarERecuperarCliente() {
        Cliente cliente = new Cliente(
                "Cliente Integração",
                "98723465741",
                LocalDate.of(2000, 1, 1),
                StatusBloqueio.A,
                BigDecimal.valueOf(1500.0)
        );

        Cliente salvo = clienteRepository.save(cliente);

        clienteIdToDelete.add(salvo.getId());

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getCpf()).isEqualTo("98723465741");

        Cliente encontrado = clienteRepository.findById(salvo.getId()).orElseThrow();
        assertThat(encontrado.getNome()).isEqualTo("Cliente Integração");
    }

    @AfterEach
    void cleanup() {
        if (clienteIdToDelete != null) {
            clienteIdToDelete.forEach(id -> clienteRepository.deleteById(id));
            clienteIdToDelete = null;
        }
    }

    @Test
    void deveBloquearClientePorFaturaAtrasada() {
        Cliente cliente = new Cliente(
                "Cliente Teste",
                "01934628192",
                LocalDate.of(2000, 1, 1),
                StatusBloqueio.A,
                BigDecimal.valueOf(1000.0)
        );
        Cliente clienteSalvo = clienteRepository.save(cliente);
        Long clienteId = clienteSalvo.getId();

        Fatura fatura = Fatura.builder()
                .cliente(clienteSalvo)
                .dataVencimento(LocalDate.now().minusDays(5))
                .valor(BigDecimal.valueOf(500.0))
                .status(StatusFatura.B)
                .build();
        Fatura faturaSalva = faturaRepository.save(fatura);

        List<FaturaResponseDTO> faturasAtrasadas = faturaService.findFaturasAtrasadas();

        assertThat(faturasAtrasadas).hasSizeGreaterThanOrEqualTo(1);
        assertThat(faturasAtrasadas.getFirst().status()).isEqualTo(StatusFatura.A);

        Cliente clienteAtualizado = clienteRepository.findById(clienteId).orElseThrow();
        assertThat(clienteAtualizado.getStatusBloqueio()).isEqualTo(StatusBloqueio.B);

        faturaRepository.deleteById(faturaSalva.getId());
        clienteRepository.deleteById(clienteId);
    }
}
