package com.desafio.sicred.votacao.config.usecases;

import com.desafio.sicred.votacao.application.core.associado.ports.output.CpfValidationOutputGateway;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindStartedSessionOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.ports.output.SaveVoteOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.usecases.RegisterVoteUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisterVoteUseCaseConfig {

    @Bean
    public RegisterVoteUseCase registerVoteUseCase(
            GetPautaByIdOutputGateway getPautaByIdOutputGateway,
            FindStartedSessionOutputGateway findOpenSessionOutputGateway,
            CpfValidationOutputGateway cpfValidationOutputGateway,
            SaveVoteOutputGateway saveVoteOutputGateway) {
        return new RegisterVoteUseCase(
                getPautaByIdOutputGateway,
                findOpenSessionOutputGateway,
                cpfValidationOutputGateway,
                saveVoteOutputGateway);
    }
}
