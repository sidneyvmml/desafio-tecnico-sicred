package com.desafio.sicred.votacao.adapters.output.sessao.adapters;

import com.desafio.sicred.votacao.adapters.output.sessao.repository.SessaoRepository;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindClosedUnpublishedSessionsOutputGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindClosedUnpublishedSessionsAdapter implements FindClosedUnpublishedSessionsOutputGateway {

    private final SessaoRepository sessaoRepository;

    @Override
    public List<Session> findClosedAndUnpublished() {
        return sessaoRepository.findClosedAndUnpublished().stream()
                .map(entity -> entity.toDomain())
                .toList();
    }
}
