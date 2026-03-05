package com.desafio.sicred.votacao.adapters.input.resultado.controllers;

import com.desafio.sicred.votacao.adapters.input.resultado.protocols.VotingResultResponse;
import com.desafio.sicred.votacao.application.core.resultado.ports.input.GetVotingResultInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/result")
@RequiredArgsConstructor
@Tag(name = "Resultado", description = "Apuração de resultado de votação")
public class GetVotingResultController {

    private final GetVotingResultInputGateway getVotingResultInputGateway;

    @GetMapping
    @Operation(summary = "Retorna o resultado da votação de uma pauta")
    public VotingResultResponse getResult(@PathVariable UUID pautaId) {
        return VotingResultResponse.from(getVotingResultInputGateway.getResult(pautaId));
    }
}
