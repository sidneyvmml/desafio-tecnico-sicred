package com.desafio.sicred.votacao.adapters.input.pauta.controllers;

import com.desafio.sicred.votacao.adapters.input.pauta.protocols.PautaResponse;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.GetPautaByIdInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas de votação")
public class GetPautaByIdController {

    private final GetPautaByIdInputGateway getPautaByIdInputGateway;

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma pauta por ID")
    public PautaResponse buscarPorId(@PathVariable UUID id) {
        return PautaResponse.from(getPautaByIdInputGateway.getById(id));
    }
}
