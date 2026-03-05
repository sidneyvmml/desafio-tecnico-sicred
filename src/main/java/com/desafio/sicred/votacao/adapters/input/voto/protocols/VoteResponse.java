package com.desafio.sicred.votacao.adapters.input.voto.protocols;

import com.desafio.sicred.votacao.application.core.voto.domain.Vote;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados do voto registrado")
public record VoteResponse(
        @Schema(description = "Identificador do voto") UUID id,
        @Schema(description = "Identificador da pauta") UUID pautaId,
        @Schema(description = "CPF do associado") String cpf,
        @Schema(description = "Opção votada") VoteOption option,
        @Schema(description = "Data/hora do registro") LocalDateTime registeredAt
) {
    public static VoteResponse from(Vote vote) {
        return new VoteResponse(
                vote.getId(),
                vote.getPautaId(),
                vote.getCpf(),
                vote.getOption(),
                vote.getRegisteredAt());
    }
}
