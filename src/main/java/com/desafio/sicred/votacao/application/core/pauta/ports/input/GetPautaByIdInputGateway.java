package com.desafio.sicred.votacao.application.core.pauta.ports.input;

import java.util.UUID;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

public interface GetPautaByIdInputGateway {
    Pauta getById(UUID id);
}
