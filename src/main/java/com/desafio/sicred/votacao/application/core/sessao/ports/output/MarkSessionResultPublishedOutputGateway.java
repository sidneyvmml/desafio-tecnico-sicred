package com.desafio.sicred.votacao.application.core.sessao.ports.output;

import java.util.UUID;

public interface MarkSessionResultPublishedOutputGateway {
    void markAsPublished(UUID sessionId);
}
