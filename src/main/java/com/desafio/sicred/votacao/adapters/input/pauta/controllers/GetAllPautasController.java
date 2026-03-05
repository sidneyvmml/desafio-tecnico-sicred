package com.desafio.sicred.votacao.adapters.input.pauta.controllers;

import com.desafio.sicred.votacao.adapters.input.pauta.protocols.PautaResponse;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.GetAllPautasInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas de votação")
public class GetAllPautasController {

    private final GetAllPautasInputGateway getAllPautasInputGateway;

    @GetMapping
    @Operation(summary = "Lista todas as pautas")
    public List<PautaResponse> listar() {
        return getAllPautasInputGateway.getAll().stream()
                .map(PautaResponse::from)
                .toList();
    }
}
