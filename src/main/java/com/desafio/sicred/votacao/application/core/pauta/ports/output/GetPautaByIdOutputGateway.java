package com.desafio.sicred.votacao.application.core.pauta.ports.output;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

import java.util.Optional;
import java.util.UUID;

public interface GetPautaByIdOutputGateway {
    Optional<Pauta> getById(UUID id);
}
