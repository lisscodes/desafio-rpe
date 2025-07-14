package com.evoluir.fintech.controllers;

import com.evoluir.fintech.domain.dtos.FaturaResponseDTO;
import com.evoluir.fintech.services.FaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Faturas", description = "Operações relacionadas às faturas dos clientes.")
@RestController
@RequestMapping("/faturas")
@RequiredArgsConstructor
public class FaturaController {

    private final FaturaService faturaService;

    @Operation(summary = "Listar faturas de um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Faturas encontradas"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<FaturaResponseDTO>> getFaturasByClienteId(
            @Parameter(description = "ID do cliente", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(faturaService.findByClienteId(id));
    }

    @Operation(
            summary = "Registrar pagamento de uma fatura",
            description = "Ao registrar o pagamento, a fatura será marcada como 'Paga' e a data de pagamento será preenchida com a data atual.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = false,
                    description = "Esse endpoint não exige corpo, mas o exemplo a seguir mostra uma possível estrutura de fatura.",
                    content = @Content(mediaType = "application/json", examples = {
                            @ExampleObject(name = "Fatura Exemplo", value = """
                    {
                      "id": 1,
                      "clienteId": 1,
                      "dataVencimento": "2024-07-15",
                      "dataPagamento": null,
                      "valor": 120.75,
                      "status": "B"
                    }
                """)
                    })
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento registrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fatura não encontrada")
    })
    @PutMapping("/{id}/pagamento")
    public ResponseEntity<FaturaResponseDTO> registrarPagamento(
            @Parameter(description = "ID da fatura", example = "1")
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(faturaService.registrarPagamento(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Listar faturas em atraso")
    @ApiResponse(responseCode = "200", description = "Faturas em atraso retornadas")
    @GetMapping("/atrasadas")
    public ResponseEntity<List<FaturaResponseDTO>> getFaturasAtrasadas() {
        return ResponseEntity.ok(faturaService.findFaturasAtrasadas());
    }
}
