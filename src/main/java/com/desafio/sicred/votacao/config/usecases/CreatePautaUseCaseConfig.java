package com.desafio.sicred.votacao.config.usecases;

import com.desafio.sicred.votacao.application.core.pauta.ports.output.CreatePautaOutputGateway;
import com.desafio.sicred.votacao.application.core.pauta.usecases.CreatePautaUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreatePautaUseCaseConfig {

    @Bean
    public CreatePautaUseCase createPautaUseCase(CreatePautaOutputGateway createPautaOutputGateway) {
        return new CreatePautaUseCase(createPautaOutputGateway);
    }
}
