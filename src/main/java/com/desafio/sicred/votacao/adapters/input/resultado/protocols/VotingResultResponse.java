package com.desafio.sicred.votacao.adapters.input.resultado.protocols;

import com.desafio.sicred.votacao.application.core.resultado.domain.ResultWinner;
import com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resultado de votação da pauta")
public record VotingResultResponse(
        @Schema(description = "Identificador da pauta") UUID pautaId,
        @Schema(description = "Votos SIM") long simCount,
        @Schema(description = "Votos NAO") long naoCount,
        @Schema(description = "Total de votos") long total,
        @Schema(description = "Resultado: SIM, NAO ou EMPATE") ResultWinner winner
) {
    public static VotingResultResponse from(VotingResult result) {
        return new VotingResultResponse(
                result.getPautaId(),
                result.getSimCount(),
                result.getNaoCount(),
                result.getTotal(),
                result.getWinner());
    }
}
