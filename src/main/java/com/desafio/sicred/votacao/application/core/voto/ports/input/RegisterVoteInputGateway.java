package com.desafio.sicred.votacao.application.core.voto.ports.input;

import com.desafio.sicred.votacao.application.core.voto.domain.RegisterVoteCommand;
import com.desafio.sicred.votacao.application.core.voto.domain.Vote;

public interface RegisterVoteInputGateway {
    Vote register(RegisterVoteCommand command);
}
