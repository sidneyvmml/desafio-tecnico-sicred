package com.desafio.sicred.votacao.adapters.input.pauta.protocols;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados da pauta cadastrada")
public record PautaResponse(
        @Schema(description = "Identificador único") UUID id,
        @Schema(description = "Título da pauta") String titulo,
        @Schema(description = "Descrição da pauta") String descricao,
        @Schema(description = "Data/hora de criação") LocalDateTime criadaEm
) {
    public static PautaResponse from(Pauta pauta) {
        return new PautaResponse(pauta.getId(), pauta.getTitulo(), pauta.getDescricao(), pauta.getCriadaEm());
    }
}

