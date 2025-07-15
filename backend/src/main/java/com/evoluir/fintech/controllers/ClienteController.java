package com.evoluir.fintech.controllers;

import com.evoluir.fintech.domain.dtos.ClienteRequestDTO;
import com.evoluir.fintech.domain.dtos.ClienteResponseDTO;
import com.evoluir.fintech.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista de todos os clientes cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClients() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClientById(
            @Parameter(description = "ID do cliente a ser buscado") @PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Criar novo cliente", description = "Cria um novo cliente com base nos dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createClient(
            @RequestBody @Valid ClienteRequestDTO dto) {
        log.info("==> POST /clientes - Dados recebidos: nome={}, cpf={}, dataNascimento={}, limite={} ",
                dto.nome(), dto.cpf(), dto.dataNascimento(), dto.limiteCredito());
        ClienteResponseDTO created = clienteService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateClient(
            @Parameter(description = "ID do cliente a ser atualizado") @PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO dto) {
        log.info("==> PUT /clientes/{} - Dados recebidos: nome={}, cpf={}, dataNascimento={}, limiteCredito={}",
                id, dto.nome(), dto.cpf(), dto.dataNascimento(), dto.limiteCredito());
        try {
            ClienteResponseDTO updated = clienteService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.warn("Cliente com id={} não encontrado para atualização", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar clientes bloqueados", description = "Retorna uma lista de todos os clientes com status bloqueado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes bloqueados retornada com sucesso")
    })
    @GetMapping("/bloqueados")
    public ResponseEntity<List<ClienteResponseDTO>> getBlockedClients() {
        return ResponseEntity.ok(clienteService.findBlockedClients());
    }
}
