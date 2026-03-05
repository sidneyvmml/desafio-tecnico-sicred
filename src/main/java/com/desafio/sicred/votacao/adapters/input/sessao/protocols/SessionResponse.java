package com.desafio.sicred.votacao.adapters.input.sessao.protocols;

import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados da sessão de votação aberta")
public record SessionResponse(
        @Schema(description = "Identificador da sessão") UUID id,
        @Schema(description = "Identificador da pauta") UUID pautaId,
        @Schema(description = "Data/hora de abertura") LocalDateTime abertura,
        @Schema(description = "Data/hora de fechamento") LocalDateTime fechamento,
        @Schema(description = "Sessão ainda está aberta") boolean aberta
) {
    public static SessionResponse from(Session session) {
        return new SessionResponse(
                session.getId(),
                session.getPautaId(),
                session.getAbertura(),
                session.getFechamento(),
                session.isAberta()
        );
    }
}
