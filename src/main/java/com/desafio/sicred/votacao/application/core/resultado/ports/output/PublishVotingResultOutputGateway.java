package com.desafio.sicred.votacao.application.core.resultado.ports.output;

import com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult;

public interface PublishVotingResultOutputGateway {
    void publish(VotingResult result);
}
