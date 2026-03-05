package com.desafio.sicred.votacao.adapters.input.voto.protocols;

import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Requisição para registro de voto")
public record RegisterVoteRequest(
        @Schema(description = "CPF do associado (somente dígitos)", example = "12345678901")
        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Schema(description = "Opção de voto", example = "SIM", allowableValues = {"SIM", "NAO"})
        @NotNull(message = "Opção de voto é obrigatória")
        VoteOption option
) {
}
