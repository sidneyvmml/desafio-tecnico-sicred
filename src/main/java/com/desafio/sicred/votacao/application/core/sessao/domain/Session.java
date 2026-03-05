package com.desafio.sicred.votacao.application.core.sessao.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Session {

    private final UUID id;
    private final UUID pautaId;
    private final LocalDateTime abertura;
    private final LocalDateTime fechamento;
    private final boolean resultadoPublicado;

    public static Session nova(UUID pautaId, Integer duracaoMinutos) {
        LocalDateTime agora = LocalDateTime.now();
        int duracao = (duracaoMinutos != null && duracaoMinutos > 0) ? duracaoMinutos : 1;
        return Session.builder()
                .id(UUID.randomUUID())
                .pautaId(pautaId)
                .abertura(agora)
                .fechamento(agora.plusMinutes(duracao))
                .resultadoPublicado(false)
                .build();
    }

    public boolean isAberta() {
        return LocalDateTime.now().isBefore(fechamento);
    }
}
