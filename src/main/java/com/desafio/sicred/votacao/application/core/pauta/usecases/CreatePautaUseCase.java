package com.desafio.sicred.votacao.application.core.pauta.usecases;

import com.desafio.sicred.votacao.application.core.pauta.domain.CreatePautaCommand;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.CreatePautaInputGateway;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.CreatePautaOutputGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreatePautaUseCase implements CreatePautaInputGateway {

    private final CreatePautaOutputGateway outputGateway;

    @Override
    public Pauta create(CreatePautaCommand command) {
        return outputGateway.create(command);
    }
}
