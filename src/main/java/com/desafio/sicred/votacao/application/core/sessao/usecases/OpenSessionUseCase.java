package com.desafio.sicred.votacao.application.core.sessao.usecases;

import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.sessao.domain.OpenSessionCommand;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessaoAlreadyOpenedException;
import com.desafio.sicred.votacao.application.core.sessao.ports.input.OpenSessionInputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.OpenSessionOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindStartedSessionOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OpenSessionUseCase implements OpenSessionInputGateway {

    private final GetPautaByIdOutputGateway getPautaByIdOutputGateway;
    private final FindStartedSessionOutputGateway findStartedSessionOutputGateway;
    private final OpenSessionOutputGateway openSessionOutputGateway;

    @Override
    public Session abrir(OpenSessionCommand command) {
        getPautaByIdOutputGateway.getById(command.pautaId())
                .orElseThrow(() -> new PautaNotFoundException("Pauta não encontrada: " + command.pautaId()));

        findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(command.pautaId())
                .ifPresent(s -> {
                    throw new SessaoAlreadyOpenedException(
                            "Já existe uma sessão aberta para a pauta: " + command.pautaId());
                });

        Session session = Session.nova(command.pautaId(), command.duracaoMinutos());
        log.info("Abrindo sessão para pauta={}, fechamento={}", command.pautaId(), session.getFechamento());
        return openSessionOutputGateway.save(session);
    }
}
