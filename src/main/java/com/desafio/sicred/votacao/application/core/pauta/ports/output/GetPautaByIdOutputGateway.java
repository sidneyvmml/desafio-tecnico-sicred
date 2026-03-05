package com.desafio.sicred.votacao.application.core.pauta.ports.output;

import java.util.Optional;
import java.util.UUID;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

public interface GetPautaByIdOutputGateway {
    Optional<Pauta> getById(UUID id);
}
