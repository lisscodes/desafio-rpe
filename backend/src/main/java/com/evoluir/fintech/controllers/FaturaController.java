package com.evoluir.fintech.controllers;

import com.evoluir.fintech.domain.dtos.FaturaRequestDTO;
import com.evoluir.fintech.domain.dtos.FaturaResponseDTO;
import com.evoluir.fintech.services.FaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/faturas")
@RequiredArgsConstructor
@Tag(name = "Faturas", description = "Endpoints para gerenciamento de faturas")
public class FaturaController {

    private final FaturaService faturaService;

    @Operation(summary = "Criar nova fatura", description = "Cria uma nova fatura com base nos dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fatura criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou cliente bloqueado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PostMapping
    public ResponseEntity<FaturaResponseDTO> createFatura(
            @RequestBody @Valid FaturaRequestDTO dto) {
        try {
            FaturaResponseDTO created = faturaService.create(dto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Listar faturas por cliente", description = "Retorna todas as faturas de um cliente específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de faturas retornada com sucesso")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<FaturaResponseDTO>> getFaturasByClienteId(
            @Parameter(description = "ID do cliente para listar as faturas") @PathVariable Long id) {
        return ResponseEntity.ok(faturaService.findByClienteId(id));
    }

    @Operation(summary = "Registrar pagamento de fatura", description = "Registra o pagamento de uma fatura pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pagamento registrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fatura não encontrada")
    })
    @PutMapping("/{id}/pagamento")
    public ResponseEntity<FaturaResponseDTO> registrarPagamento(
            @Parameter(description = "ID da fatura para registrar pagamento") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(faturaService.registrarPagamento(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar faturas atrasadas", description = "Retorna todas as faturas atrasadas e atualiza o status do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de faturas atrasadas retornada com sucesso")
    })
    @GetMapping("/atrasadas")
    public ResponseEntity<List<FaturaResponseDTO>> getFaturasAtrasadas() {
        return ResponseEntity.ok(faturaService.findFaturasAtrasadas());
    }
}
