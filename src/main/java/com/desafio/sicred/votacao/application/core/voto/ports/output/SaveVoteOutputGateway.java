package com.desafio.sicred.votacao.application.core.voto.ports.output;

import com.desafio.sicred.votacao.application.core.voto.domain.Vote;

public interface SaveVoteOutputGateway {
    Vote save(Vote vote);
}
