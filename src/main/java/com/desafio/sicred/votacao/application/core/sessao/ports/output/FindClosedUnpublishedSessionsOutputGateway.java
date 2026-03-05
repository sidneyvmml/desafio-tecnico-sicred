package com.desafio.sicred.votacao.application.core.sessao.ports.output;

import com.desafio.sicred.votacao.application.core.sessao.domain.Session;

import java.util.List;

public interface FindClosedUnpublishedSessionsOutputGateway {
    List<Session> findClosedAndUnpublished();
}
