package com.evoluir.fintech.services;

import com.evoluir.fintech.domain.dtos.ClienteRequestDTO;
import com.evoluir.fintech.domain.dtos.ClienteResponseDTO;
import com.evoluir.fintech.domain.entities.Cliente;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import com.evoluir.fintech.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private ClienteRepository clienteRepository;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteRepository = mock(ClienteRepository.class);
        clienteService = new ClienteService(clienteRepository);
    }

    @Test
    void deveCriarClienteComStatusAtivo() {
        ClienteRequestDTO dto = new ClienteRequestDTO("Lis", "12345678900", LocalDate.of(1998, 10, 15), 2000.0);

        when(clienteRepository.save(any())).thenAnswer(i -> {
            Cliente c = i.getArgument(0);
            c.setId(1L);
            return c;
        });

        ClienteResponseDTO response = clienteService.create(dto);

        assertThat(response).isNotNull();
        assertThat(response.statusBloqueio()).isEqualTo(StatusBloqueio.A);
        assertThat(response.nome()).isEqualTo("Lis");
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = new Cliente("Lis", "12345678900", LocalDate.of(1998, 10, 15), StatusBloqueio.A, 2000.0);
        cliente.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<ClienteResponseDTO> resultado = clienteService.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().cpf()).isEqualTo("12345678900");
    }

    @Test
    void deveAtualizarCliente() {
        Cliente existente = new Cliente("Antigo", "00000000000", LocalDate.of(2000, 1, 1), StatusBloqueio.A, 1000.0);
        existente.setId(1L);

        ClienteRequestDTO novo = new ClienteRequestDTO("Novo", "11111111111", LocalDate.of(1995, 5, 5), 5000.0);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ClienteResponseDTO atualizado = clienteService.update(1L, novo);

        assertThat(atualizado.nome()).isEqualTo("Novo");
        assertThat(atualizado.cpf()).isEqualTo("11111111111");
        assertThat(atualizado.limiteCredito()).isEqualTo(5000.0);
    }

    @Test
    void deveListarTodosClientes() {
        Cliente c1 = new Cliente("Lis", "123", LocalDate.now(), StatusBloqueio.A, 1000.0);
        Cliente c2 = new Cliente("Ana", "456", LocalDate.now(), StatusBloqueio.B, 0.0);

        when(clienteRepository.findAll()).thenReturn(List.of(c1, c2));

        List<ClienteResponseDTO> lista = clienteService.findAll();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).nome()).isEqualTo("Lis");
    }

    @Test
    void deveListarClientesBloqueados() {
        Cliente bloqueado = new Cliente("Bloqueado", "789", LocalDate.now(), StatusBloqueio.B, 0.0);
        when(clienteRepository.findByStatusBloqueio(StatusBloqueio.B)).thenReturn(List.of(bloqueado));

        List<ClienteResponseDTO> bloqueados = clienteService.findBlockedClients();

        assertThat(bloqueados).hasSize(1);
        assertThat(bloqueados.get(0).statusBloqueio()).isEqualTo(StatusBloqueio.B);
    }
}
