package com.evoluir.fintech.services;

import com.evoluir.fintech.domain.dtos.FaturaRequestDTO;
import com.evoluir.fintech.domain.dtos.FaturaResponseDTO;
import com.evoluir.fintech.domain.entities.Cliente;
import com.evoluir.fintech.domain.entities.Fatura;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import com.evoluir.fintech.domain.entities.StatusFatura;
import com.evoluir.fintech.repositories.ClienteRepository;
import com.evoluir.fintech.repositories.FaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FaturaServiceTest {

    @Mock
    private FaturaRepository faturaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private FaturaService faturaService;

    private Cliente cliente;
    private Fatura fatura;
    private FaturaRequestDTO faturaRequestDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Lis", "12345678900", LocalDate.of(1998, 10, 15), StatusBloqueio.A, BigDecimal.valueOf(2000.00));
        cliente.setId(1L);

        fatura = Fatura.builder()
                .id(1L)
                .cliente(cliente)
                .dataVencimento(LocalDate.of(2025, 7, 15))
                .valor(BigDecimal.valueOf(150.00))
                .status(StatusFatura.B)
                .build();

        faturaRequestDTO = new FaturaRequestDTO(1L, LocalDate.of(2025, 7, 15), BigDecimal.valueOf(150.00));
    }

    @Test
    void deveCriarFaturaComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(faturaRepository.save(any(Fatura.class))).thenReturn(fatura);

        FaturaResponseDTO response = faturaService.create(faturaRequestDTO);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.clienteId()).isEqualTo(1L);
        assertThat(response.valor()).isEqualTo(BigDecimal.valueOf(150.00));
        assertThat(response.status()).isEqualTo(StatusFatura.B);
        verify(faturaRepository).save(any(Fatura.class));
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> faturaService.create(faturaRequestDTO));

        assertThat(exception.getMessage()).isEqualTo("Cliente não encontrado");
        verify(faturaRepository, never()).save(any(Fatura.class));
    }

    @Test
    void deveLancarExcecaoQuandoClienteBloqueado() {
        cliente.setStatusBloqueio(StatusBloqueio.B);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> faturaService.create(faturaRequestDTO));

        assertThat(exception.getMessage()).isEqualTo("Não é possível criar fatura para cliente bloqueado");
        verify(faturaRepository, never()).save(any(Fatura.class));
    }

    @Test
    void deveLancarExcecaoQuandoValorExcedeLimiteCredito() {
        faturaRequestDTO = new FaturaRequestDTO(1L, LocalDate.of(2025, 7, 15), BigDecimal.valueOf(2500.00));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> faturaService.create(faturaRequestDTO));

        assertThat(exception.getMessage()).isEqualTo("Valor da fatura excede o limite de crédito do cliente");
        verify(faturaRepository, never()).save(any(Fatura.class));
    }

    @Test
    void deveListarFaturasPorClienteId() {
        when(faturaRepository.findByClienteId(1L)).thenReturn(List.of(fatura));

        List<FaturaResponseDTO> result = faturaService.findByClienteId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).clienteId()).isEqualTo(1L);
        assertThat(result.get(0).status()).isEqualTo(StatusFatura.B);
        verify(faturaRepository).findByClienteId(1L);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaFaturas() {
        when(faturaRepository.findByClienteId(1L)).thenReturn(List.of());

        List<FaturaResponseDTO> result = faturaService.findByClienteId(1L);

        assertThat(result).isEmpty();
        verify(faturaRepository).findByClienteId(1L);
    }

    @Test
    void deveRegistrarPagamentoComSucesso() {
        when(faturaRepository.findById(1L)).thenReturn(Optional.of(fatura));
        when(faturaRepository.save(any(Fatura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FaturaResponseDTO response = faturaService.registrarPagamento(1L);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(StatusFatura.P);
        assertThat(response.dataPagamento()).isEqualTo(LocalDate.now());
        verify(faturaRepository).save(any(Fatura.class));
    }

    @Test
    void deveLancarExcecaoQuandoFaturaNaoEncontrada() {
        when(faturaRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> faturaService.registrarPagamento(1L));

        assertThat(exception.getMessage()).isEqualTo("Fatura não encontrada");
        verify(faturaRepository, never()).save(any(Fatura.class));
    }

    @Test
    void deveListarFaturasAtrasadasEBloquearClientes() {
        fatura.setDataVencimento(LocalDate.now().minusDays(5));
        when(faturaRepository.findFaturasAtrasadas(any(LocalDate.class))).thenReturn(List.of(fatura));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(faturaRepository.save(any(Fatura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<FaturaResponseDTO> result = faturaService.findFaturasAtrasadas();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(StatusFatura.A);
        assertThat(cliente.getStatusBloqueio()).isEqualTo(StatusBloqueio.B);
        assertThat(cliente.getLimiteCredito()).isEqualTo(BigDecimal.ZERO);
        verify(faturaRepository).findFaturasAtrasadas(LocalDate.now().minusDays(3));
        verify(clienteRepository).save(any(Cliente.class));
        verify(faturaRepository).save(any(Fatura.class));
    }

    @Test
    void deveIgnorarClientesJaBloqueadosAoListarFaturasAtrasadas() {
        cliente.setStatusBloqueio(StatusBloqueio.B);
        cliente.setLimiteCredito(BigDecimal.ZERO);
        fatura.setDataVencimento(LocalDate.now().minusDays(5));
        when(faturaRepository.findFaturasAtrasadas(any(LocalDate.class))).thenReturn(List.of(fatura));
        when(faturaRepository.save(any(Fatura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<FaturaResponseDTO> result = faturaService.findFaturasAtrasadas();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(StatusFatura.A);
        assertThat(cliente.getStatusBloqueio()).isEqualTo(StatusBloqueio.B);
        assertThat(cliente.getLimiteCredito()).isEqualTo(BigDecimal.ZERO);
        verify(clienteRepository, never()).save(any(Cliente.class));
        verify(faturaRepository).save(any(Fatura.class));
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaFaturasAtrasadas() {
        when(faturaRepository.findFaturasAtrasadas(any(LocalDate.class))).thenReturn(List.of());

        List<FaturaResponseDTO> result = faturaService.findFaturasAtrasadas();

        assertThat(result).isEmpty();
        verify(faturaRepository).findFaturasAtrasadas(LocalDate.now().minusDays(3));
        verify(clienteRepository, never()).save(any(Cliente.class));
        verify(faturaRepository, never()).save(any(Fatura.class));
    }

    @Test
    void deveLancarExcecaoQuandoFaturaRequestDTOInvalido() {
        FaturaRequestDTO dtoInvalido = new FaturaRequestDTO(null, LocalDate.of(2025, 7, 15), BigDecimal.valueOf(150.00));
        FaturaRequestDTO finalDtoInvalido = dtoInvalido;
        assertThrows(IllegalArgumentException.class, () -> faturaService.create(finalDtoInvalido),
                "Deve lançar exceção para clienteId nulo");

        dtoInvalido = new FaturaRequestDTO(1L, LocalDate.of(2025, 7, 15), BigDecimal.valueOf(-150.00));
        FaturaRequestDTO finalDtoInvalido1 = dtoInvalido;
        assertThrows(IllegalArgumentException.class, () -> faturaService.create(finalDtoInvalido1),
                "Deve lançar exceção para valor negativo");
    }

    @Test
    void deveLancarExcecaoQuandoRepositorioFalhaAoSalvar() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(faturaRepository.save(any(Fatura.class))).thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> faturaService.create(faturaRequestDTO));
        assertThat(exception.getMessage()).isEqualTo("Erro no banco");
        verify(faturaRepository).save(any(Fatura.class));
    }

    @Test
    void deveIgnorarFaturasComMenosDeTresDiasDeAtraso() {
        fatura.setDataVencimento(LocalDate.now().minusDays(2));
        when(faturaRepository.findFaturasAtrasadas(any(LocalDate.class))).thenReturn(List.of());

        List<FaturaResponseDTO> result = faturaService.findFaturasAtrasadas();

        assertThat(result).isEmpty();
        verify(faturaRepository).findFaturasAtrasadas(LocalDate.now().minusDays(3));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}