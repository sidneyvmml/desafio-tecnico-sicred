package com.desafio.sicred.votacao.adapters.input.pauta.controllers;

import com.desafio.sicred.votacao.adapters.input.pauta.protocols.PautaResponse;
import com.desafio.sicred.votacao.application.core.common.PageRequest;
import com.desafio.sicred.votacao.application.core.common.PageResult;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.GetAllPautasInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas de votação")
public class GetAllPautasController {

    private final GetAllPautasInputGateway getAllPautasInputGateway;

    @GetMapping
    @Operation(summary = "Lista todas as pautas (paginado)")
    public PageResult<PautaResponse> listar(
            @Parameter(description = "Número da página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página (1–100)", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return getAllPautasInputGateway.getAll(pageRequest)
                .map(PautaResponse::from);
    }
}
