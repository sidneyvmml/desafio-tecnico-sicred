package com.desafio.sicred.votacao.adapters.output.sessao.adapters;

import com.desafio.sicred.votacao.adapters.output.sessao.repository.SessaoRepository;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.MarkSessionResultPublishedOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarkSessionResultPublishedAdapter implements MarkSessionResultPublishedOutputGateway {

    private final SessaoRepository sessaoRepository;

    @Override
    public void markAsPublished(UUID sessionId) {
        sessaoRepository.markResultadoPublicado(sessionId);
    }
}
