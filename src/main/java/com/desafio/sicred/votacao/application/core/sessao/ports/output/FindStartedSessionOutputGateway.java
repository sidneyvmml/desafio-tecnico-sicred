package com.desafio.sicred.votacao.application.core.sessao.ports.output;

import com.desafio.sicred.votacao.application.core.sessao.domain.Session;

import java.util.Optional;
import java.util.UUID;

public interface FindStartedSessionOutputGateway {
    Optional<Session> buscarSessaoAbertaPorPauta(UUID pautaId);
}
