package com.desafio.sicred.votacao.config.usecases;

import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.OpenSessionOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindStartedSessionOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.usecases.OpenSessionUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenSessionUseCaseConfig {

    @Bean
    public OpenSessionUseCase abrirSessaoUseCase(
            GetPautaByIdOutputGateway getPautaByIdOutputGateway,
            FindStartedSessionOutputGateway findStartedSessionOutputGateway,
            OpenSessionOutputGateway openSessionOutputGateway) {
        return new OpenSessionUseCase(
                getPautaByIdOutputGateway,
                findStartedSessionOutputGateway,
                openSessionOutputGateway);
    }
}
