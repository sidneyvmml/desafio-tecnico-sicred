package com.desafio.sicred.votacao.application.core.pauta.usecases;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.GetPautaByIdInputGateway;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class GetPautaByIdUseCase implements GetPautaByIdInputGateway {

    private final GetPautaByIdOutputGateway outputGateway;

    @Override
    public Pauta getById(UUID id) {
        return outputGateway.getById(id)
                .orElseThrow(() -> new PautaNotFoundException("Pauta não encontrada: " + id));
    }
}
