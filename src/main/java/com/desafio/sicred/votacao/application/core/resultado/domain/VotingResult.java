package com.desafio.sicred.votacao.application.core.resultado.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class VotingResult {

    private final UUID pautaId;
    private final long simCount;
    private final long naoCount;
    private final long total;
    private final ResultWinner winner;

    public static VotingResult of(UUID pautaId, long simCount, long naoCount) {
        ResultWinner winner;
        if (simCount > naoCount) {
            winner = ResultWinner.SIM;
        } else if (naoCount > simCount) {
            winner = ResultWinner.NAO;
        } else {
            winner = ResultWinner.EMPATE;
        }
        return VotingResult.builder()
                .pautaId(pautaId)
                .simCount(simCount)
                .naoCount(naoCount)
                .total(simCount + naoCount)
                .winner(winner)
                .build();
    }
}
