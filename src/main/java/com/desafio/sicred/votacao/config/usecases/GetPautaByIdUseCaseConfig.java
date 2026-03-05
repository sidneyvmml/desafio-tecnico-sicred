package com.desafio.sicred.votacao.config.usecases;

import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.pauta.usecases.GetPautaByIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetPautaByIdUseCaseConfig {

    @Bean
    public GetPautaByIdUseCase getPautaByIdUseCase(GetPautaByIdOutputGateway getPautaByIdOutputGateway) {
        return new GetPautaByIdUseCase(getPautaByIdOutputGateway);
    }
}
