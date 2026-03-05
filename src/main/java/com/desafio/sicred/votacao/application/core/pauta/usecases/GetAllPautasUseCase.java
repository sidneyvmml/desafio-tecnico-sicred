package com.desafio.sicred.votacao.application.core.pauta.usecases;

import com.desafio.sicred.votacao.application.core.common.PageRequest;
import com.desafio.sicred.votacao.application.core.common.PageResult;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.input.GetAllPautasInputGateway;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetAllPautasOutputGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllPautasUseCase implements GetAllPautasInputGateway {

    private final GetAllPautasOutputGateway outputGateway;

    @Override
    public PageResult<Pauta> getAll(PageRequest pageRequest) {
        return outputGateway.getAll(pageRequest);
    }
}
