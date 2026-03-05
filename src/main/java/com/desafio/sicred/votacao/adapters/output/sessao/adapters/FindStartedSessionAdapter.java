package com.desafio.sicred.votacao.adapters.output.sessao.adapters;

import com.desafio.sicred.votacao.adapters.output.sessao.repository.SessaoRepository;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindStartedSessionOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindStartedSessionAdapter implements FindStartedSessionOutputGateway {

    private final SessaoRepository sessaoRepository;

    @Override
    public Optional<Session> buscarSessaoAbertaPorPauta(UUID pautaId) {
        log.info("Verificando sessão aberta para pauta={}", pautaId);
        return sessaoRepository.findByPautaIdAndFechamentoAfter(pautaId)
                .map(entity -> entity.toDomain());
    }
}
