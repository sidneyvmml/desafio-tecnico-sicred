package com.desafio.sicred.votacao.application.core.sessao.ports.input;

import com.desafio.sicred.votacao.application.core.sessao.domain.OpenSessionCommand;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;

public interface OpenSessionInputGateway {
    Session abrir(OpenSessionCommand command);
}
