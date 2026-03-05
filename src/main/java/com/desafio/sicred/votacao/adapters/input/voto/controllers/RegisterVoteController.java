package com.desafio.sicred.votacao.adapters.input.voto.controllers;

import com.desafio.sicred.votacao.adapters.input.voto.protocols.RegisterVoteRequest;
import com.desafio.sicred.votacao.adapters.input.voto.protocols.VoteResponse;
import com.desafio.sicred.votacao.application.core.voto.domain.RegisterVoteCommand;
import com.desafio.sicred.votacao.application.core.voto.ports.input.RegisterVoteInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/votes")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Registro de votos em sessões de votação")
public class RegisterVoteController {

    private final RegisterVoteInputGateway registerVoteInputGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra o voto de um associado em uma pauta")
    public VoteResponse register(
            @PathVariable UUID pautaId,
            @Valid @RequestBody RegisterVoteRequest request) {
        RegisterVoteCommand command = new RegisterVoteCommand(pautaId, request.cpf(), request.option());
        return VoteResponse.from(registerVoteInputGateway.register(command));
    }
}
