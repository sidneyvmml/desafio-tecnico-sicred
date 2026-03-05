package com.desafio.sicred.votacao.adapters.input.pauta.protocols;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para cadastro de uma nova pauta")
public record CreatePautaRequest(

        @Schema(description = "Título da pauta", example = "Aprovação do orçamento 2026")
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String titulo,

        @Schema(description = "Descrição opcional da pauta", example = "Votação referente ao orçamento anual")
        String descricao
) {}