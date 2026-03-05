package com.desafio.sicred.votacao.application.core.resultado.usecases;

import com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.CountVotesOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.PublishVotingResultOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindClosedUnpublishedSessionsOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.MarkSessionResultPublishedOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PublishClosedSessionResultsUseCase {

    private final FindClosedUnpublishedSessionsOutputGateway findClosedUnpublishedSessionsOutputGateway;
    private final CountVotesOutputGateway countVotesOutputGateway;
    private final PublishVotingResultOutputGateway publishVotingResultOutputGateway;
    private final MarkSessionResultPublishedOutputGateway markSessionResultPublishedOutputGateway;

    public void execute() {
        List<Session> sessions = findClosedUnpublishedSessionsOutputGateway.findClosedAndUnpublished();

        if (sessions.isEmpty()) {
            return;
        }

        log.info("Processando {} sessão(ões) encerrada(s) sem resultado publicado", sessions.size());

        for (Session session : sessions) {
            try {
                long sim = countVotesOutputGateway.countByOption(session.getPautaId(), VoteOption.SIM);
                long nao = countVotesOutputGateway.countByOption(session.getPautaId(), VoteOption.NAO);

                VotingResult result = VotingResult.of(session.getPautaId(), sim, nao);
                publishVotingResultOutputGateway.publish(result);
                markSessionResultPublishedOutputGateway.markAsPublished(session.getId());

                log.info("Resultado publicado: pautaId={}, SIM={}, NAO={}, vencedor={}",
                        session.getPautaId(), sim, nao, result.getWinner());
            } catch (Exception ex) {
                log.error("Falha ao publicar resultado da sessão {}: {}", session.getId(), ex.getMessage(), ex);
            }
        }
    }
}
