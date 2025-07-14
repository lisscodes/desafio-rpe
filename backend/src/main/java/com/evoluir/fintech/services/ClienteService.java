package com.evoluir.fintech.services;

import com.evoluir.fintech.domain.dtos.ClienteRequestDTO;
import com.evoluir.fintech.domain.dtos.ClienteResponseDTO;
import com.evoluir.fintech.domain.entities.Cliente;
import com.evoluir.fintech.domain.entities.StatusBloqueio;
import com.evoluir.fintech.repositories.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public Optional<ClienteResponseDTO> findById(Long id) {
        return clienteRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public ClienteResponseDTO create(ClienteRequestDTO dto) {
        Cliente novo = new Cliente();
        novo.setNome(dto.getNome());
        novo.setCpf(dto.getCpf());
        novo.setDataNascimento(dto.getDataNascimento());
        novo.setLimiteCredito(dto.getLimiteCredito());
        novo.setStatusBloqueio(StatusBloqueio.A);
        Cliente salvo = clienteRepository.save(novo);
        return toResponseDTO(salvo);
    }

    public ClienteResponseDTO update(Long id, ClienteRequestDTO dto) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        existente.setNome(dto.getNome());
        existente.setCpf(dto.getCpf());
        existente.setDataNascimento(dto.getDataNascimento());
        existente.setLimiteCredito(dto.getLimiteCredito());

        Cliente atualizado = clienteRepository.save(existente);
        return toResponseDTO(atualizado);
    }

    public List<ClienteResponseDTO> findBlockedClients() {
        return clienteRepository.findByStatusBloqueio(StatusBloqueio.B)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private ClienteResponseDTO toResponseDTO(Cliente c) {
        return new ClienteResponseDTO(
                c.getId(),
                c.getNome(),
                c.getCpf(),
                c.getDataNascimento(),
                c.getStatusBloqueio(),
                c.getLimiteCredito()
        );
    }
}
