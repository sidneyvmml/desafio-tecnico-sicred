package com.desafio.sicred.votacao.config.usecases;

import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetAllPautasOutputGateway;
import com.desafio.sicred.votacao.application.core.pauta.usecases.GetAllPautasUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetAllPautasUseCaseConfig {

    @Bean
    public GetAllPautasUseCase getAllPautasUseCase(GetAllPautasOutputGateway getAllPautasOutputGateway) {
        return new GetAllPautasUseCase(getAllPautasOutputGateway);
    }
}
