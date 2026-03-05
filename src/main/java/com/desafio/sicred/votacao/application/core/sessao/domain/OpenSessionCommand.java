package com.desafio.sicred.votacao.application.core.sessao.domain;

import java.util.UUID;

public record OpenSessionCommand(UUID pautaId, Integer duracaoMinutos) {
}
