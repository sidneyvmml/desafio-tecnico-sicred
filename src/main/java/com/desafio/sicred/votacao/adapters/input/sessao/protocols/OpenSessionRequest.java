package com.desafio.sicred.votacao.adapters.input.sessao.protocols;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requisição para abertura de sessão de votação")
public record OpenSessionRequest(
        @Schema(description = "Duração da sessão em minutos (default: 1)", example = "5")
        Integer duracaoMinutos
) {
}
