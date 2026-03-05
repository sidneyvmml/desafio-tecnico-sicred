package com.desafio.sicred.votacao.application.core.resultado.ports.input;

import com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult;

import java.util.UUID;

public interface GetVotingResultInputGateway {
    VotingResult getResult(UUID pautaId);
}
