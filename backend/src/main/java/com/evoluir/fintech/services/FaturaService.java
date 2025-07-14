package com.evoluir.fintech.services;

import com.evoluir.fintech.domain.dtos.FaturaResponseDTO;
import com.evoluir.fintech.domain.entities.Cliente;
import com.evoluir.fintech.domain.entities.Fatura;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import com.evoluir.fintech.domain.entities.StatusFatura;
import com.evoluir.fintech.repositories.ClienteRepository;
import com.evoluir.fintech.repositories.FaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FaturaService {

    private final FaturaRepository faturaRepository;
    private final ClienteRepository clienteRepository;

    public List<FaturaResponseDTO> findByClienteId(Long clienteId) {
        return faturaRepository.findByClienteId(clienteId).stream()
                .map(this::toDTO)
                .toList();
    }

    public FaturaResponseDTO registrarPagamento(Long faturaId) {
        Fatura fatura = faturaRepository.findById(faturaId)
                .orElseThrow(() -> new RuntimeException("Fatura não encontrada"));

        fatura.setDataPagamento(LocalDate.now());
        fatura.setStatus(StatusFatura.P);

        return toDTO(faturaRepository.save(fatura));
    }

    public List<FaturaResponseDTO> findFaturasAtrasadas() {
        List<Fatura> atrasadas = faturaRepository.findFaturasAtrasadas(LocalDate.now().minusDays(3));

        atrasadas.forEach(fatura -> {
            Cliente cliente = fatura.getCliente();
            if (cliente.getStatusBloqueio() != StatusBloqueio.B) {
                cliente.setStatusBloqueio(StatusBloqueio.B);
                cliente.setLimiteCredito(0.0);
                clienteRepository.save(cliente);
            }
        });

        return atrasadas.stream()
                .map(this::toDTO)
                .toList();
    }

    private FaturaResponseDTO toDTO(Fatura f) {
        return new FaturaResponseDTO(
                f.getId(),
                f.getCliente().getId(),
                f.getDataVencimento(),
                f.getDataPagamento(),
                f.getValor(),
                f.getStatus()
        );
    }
}
