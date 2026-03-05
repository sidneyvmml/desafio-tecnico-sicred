package com.desafio.sicred.votacao.application.core.pauta.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Pauta {

    private final UUID id;
    private final String titulo;
    private final String descricao;
    private final LocalDateTime criadaEm;

    public static Pauta nova(String titulo, String descricao) {
        return new Pauta(UUID.randomUUID(), titulo, descricao, LocalDateTime.now());
    }

}
