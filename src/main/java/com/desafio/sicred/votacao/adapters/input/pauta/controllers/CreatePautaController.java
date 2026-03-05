package com.desafio.sicred.votacao.adapters.input.pauta.controllers;


import com.desafio.sicred.votacao.adapters.input.pauta.protocols.CreatePautaRequest;
import com.desafio.sicred.votacao.adapters.input.pauta.protocols.PautaResponse;
import com.desafio.sicred.votacao.application.core.pauta.domain.CreatePautaCommand;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.CreatePautaInputGateway;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
@Tag(name = "Pautas", description = "Gerenciamento de pautas de votação")
public class CreatePautaController {

    private final CreatePautaInputGateway createPautaInputGateway;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra uma nova pauta")
    public PautaResponse cadastrar(@Valid @RequestBody CreatePautaRequest request) {
        Pauta pauta = createPautaInputGateway.create(new CreatePautaCommand(request.titulo(), request.descricao()));
        return PautaResponse.from(pauta);
    }

}
