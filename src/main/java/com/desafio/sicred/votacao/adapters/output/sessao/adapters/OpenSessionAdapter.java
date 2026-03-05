package com.desafio.sicred.votacao.adapters.output.sessao.adapters;

import com.desafio.sicred.votacao.adapters.output.sessao.entities.SessaoEntity;
import com.desafio.sicred.votacao.adapters.output.sessao.repository.SessaoRepository;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.OpenSessionOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSessionAdapter implements OpenSessionOutputGateway {

    private final SessaoRepository sessaoRepository;

    @Override
    public Session save(Session session) {
        log.info("Persistindo sessão id={}", session.getId());
        SessaoEntity entity = SessaoEntity.fromDomain(session);
        return sessaoRepository.save(entity).toDomain();
    }
}
