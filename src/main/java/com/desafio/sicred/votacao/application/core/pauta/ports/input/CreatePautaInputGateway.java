package com.desafio.sicred.votacao.application.core.pauta.ports.input;

import com.desafio.sicred.votacao.application.core.pauta.domain.CreatePautaCommand;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

public interface CreatePautaInputGateway {
    Pauta create(CreatePautaCommand command);
}
