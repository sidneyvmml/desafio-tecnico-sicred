package com.desafio.sicred.votacao.application.core.sessao.ports.output;

import com.desafio.sicred.votacao.application.core.sessao.domain.Session;

public interface OpenSessionOutputGateway {
    Session save(Session session);
}
