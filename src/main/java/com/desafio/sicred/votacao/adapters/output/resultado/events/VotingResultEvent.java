package com.desafio.sicred.votacao.adapters.output.resultado.events;

import com.desafio.sicred.votacao.application.core.resultado.domain.ResultWinner;
import com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult;

import java.time.LocalDateTime;
import java.util.UUID;

public record VotingResultEvent(
        UUID pautaId,
        long simCount,
        long naoCount,
        long total,
        ResultWinner winner,
        LocalDateTime publishedAt
) {
    public static VotingResultEvent from(VotingResult result) {
        return new VotingResultEvent(
                result.getPautaId(),
                result.getSimCount(),
                result.getNaoCount(),
                result.getTotal(),
                result.getWinner(),
                LocalDateTime.now()
        );
    }
}
