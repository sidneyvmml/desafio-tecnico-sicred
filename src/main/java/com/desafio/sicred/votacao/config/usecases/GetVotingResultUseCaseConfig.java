package com.desafio.sicred.votacao.config.usecases;

import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.CountVotesOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.usecases.GetVotingResultUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetVotingResultUseCaseConfig {

    @Bean
    public GetVotingResultUseCase getVotingResultUseCase(
            GetPautaByIdOutputGateway getPautaByIdOutputGateway,
            CountVotesOutputGateway countVotesOutputGateway) {
        return new GetVotingResultUseCase(getPautaByIdOutputGateway, countVotesOutputGateway);
    }
}
