package com.desafio.sicred.votacao.application.core.voto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class Vote {

    private final UUID id;
    private final UUID pautaId;
    private final String cpf;
    private final VoteOption option;
    private final LocalDateTime registeredAt;

    public static Vote create(UUID pautaId, String cpf, VoteOption option) {
        return Vote.builder()
                .id(UUID.randomUUID())
                .pautaId(pautaId)
                .cpf(cpf)
                .option(option)
                .registeredAt(LocalDateTime.now())
                .build();
    }
}
