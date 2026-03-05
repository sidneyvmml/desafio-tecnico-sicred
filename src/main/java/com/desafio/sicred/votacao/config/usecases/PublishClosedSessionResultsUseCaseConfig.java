package com.desafio.sicred.votacao.config.usecases;

import com.desafio.sicred.votacao.application.core.resultado.ports.output.CountVotesOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.PublishVotingResultOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.usecases.PublishClosedSessionResultsUseCase;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindClosedUnpublishedSessionsOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.MarkSessionResultPublishedOutputGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublishClosedSessionResultsUseCaseConfig {

    @Bean
    public PublishClosedSessionResultsUseCase publishClosedSessionResultsUseCase(
            FindClosedUnpublishedSessionsOutputGateway findClosedUnpublishedSessionsOutputGateway,
            CountVotesOutputGateway countVotesOutputGateway,
            PublishVotingResultOutputGateway publishVotingResultOutputGateway,
            MarkSessionResultPublishedOutputGateway markSessionResultPublishedOutputGateway) {
        return new PublishClosedSessionResultsUseCase(
                findClosedUnpublishedSessionsOutputGateway,
                countVotesOutputGateway,
                publishVotingResultOutputGateway,
                markSessionResultPublishedOutputGateway);
    }
}
