package com.desafio.sicred.votacao.adapters.input.sessao.controllers;

import com.desafio.sicred.votacao.adapters.input.sessao.protocols.OpenSessionRequest;
import com.desafio.sicred.votacao.adapters.input.sessao.protocols.SessionResponse;
import com.desafio.sicred.votacao.application.core.sessao.domain.OpenSessionCommand;
import com.desafio.sicred.votacao.application.core.sessao.ports.input.OpenSessionInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pautas/{pautaId}/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessões", description = "Gerenciamento de sessões de votação")
public class OpenSessionController {

    private final OpenSessionInputGateway openSessionInputGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abre uma sessão de votação para a pauta")
    public SessionResponse abrir(
            @PathVariable UUID pautaId,
            @RequestBody(required = false) OpenSessionRequest request) {
        Integer duracao = request != null ? request.duracaoMinutos() : null;
        return SessionResponse.from(
                openSessionInputGateway.abrir(new OpenSessionCommand(pautaId, duracao)));
    }
}
