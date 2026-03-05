package com.desafio.sicred.votacao.application.core.pauta.ports.output;

import com.desafio.sicred.votacao.application.core.pauta.domain.CreatePautaCommand;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

public interface CreatePautaOutputGateway {
    Pauta create(CreatePautaCommand command);
}
