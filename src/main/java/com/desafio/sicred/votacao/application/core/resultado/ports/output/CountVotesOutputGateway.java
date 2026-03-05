package com.desafio.sicred.votacao.application.core.resultado.ports.output;

import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;

import java.util.UUID;

public interface CountVotesOutputGateway {
    long countByOption(UUID pautaId, VoteOption option);
}
