package com.desafio.sicred.votacao.application.core.pauta.usecases;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.GetAllPautasInputGateway;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetAllPautasOutputGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetAllPautasUseCase implements GetAllPautasInputGateway {

    private final GetAllPautasOutputGateway outputGateway;

    @Override
    public List<Pauta> getAll() {
        return outputGateway.getAll();
    }
}
