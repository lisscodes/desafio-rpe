package com.evoluir.fintech.controllers;

import com.evoluir.fintech.domain.dtos.ClienteRequestDTO;
import com.evoluir.fintech.domain.dtos.ClienteResponseDTO;
import com.evoluir.fintech.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clientes", description = "Operações relacionadas ao gerenciamento de clientes.")
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Listar todos os clientes")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClients() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getClientById(
            @Parameter(description = "ID do cliente", example = "1")
            @PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Cadastrar novo cliente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do cliente para cadastro",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Cliente Exemplo", value = """
                            {
                              "nome": "Lisandra Dias",
                              "cpf": "12345678901",
                              "dataNascimento": "1998-10-15",
                              "limiteCredito": 2000.00
                            }
                            """)
                    })
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createClient(
            @org.springframework.web.bind.annotation.RequestBody @Valid ClienteRequestDTO dto) {

        log.info("==> POST /clientes - Dados recebidos: nome={}, cpf={}, dataNascimento={}, limiteCredito={}",
                dto.getNome(), dto.getCpf(), dto.getDataNascimento(), dto.getLimiteCredito());

        ClienteResponseDTO created = clienteService.create(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Atualizar dados de um cliente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados atualizados do cliente",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Cliente Atualizado", value = """
                            {
                              "nome": "Lis Dias",
                              "cpf": "32165498700",
                              "dataNascimento": "2000-05-20",
                              "limiteCredito": 1500.00
                            }
                            """)
                    })
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateClient(
            @Parameter(description = "ID do cliente", example = "1")
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody @Valid ClienteRequestDTO dto) {

        log.info("==> PUT /clientes/{} - Dados recebidos: nome={}, cpf={}, dataNascimento={}, limiteCredito={}",
                id, dto.getNome(), dto.getCpf(), dto.getDataNascimento(), dto.getLimiteCredito());

        try {
            ClienteResponseDTO updated = clienteService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.warn("Cliente com id={} não encontrado para atualização", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar clientes bloqueados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes bloqueados")
    @GetMapping("/bloqueados")
    public ResponseEntity<List<ClienteResponseDTO>> getBlockedClients() {
        return ResponseEntity.ok(clienteService.findBlockedClients());
    }
}
