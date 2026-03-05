package com.desafio.sicred.votacao.application.core.voto.domain;

import java.util.UUID;

public record RegisterVoteCommand(UUID pautaId, String cpf, VoteOption option) {
}
