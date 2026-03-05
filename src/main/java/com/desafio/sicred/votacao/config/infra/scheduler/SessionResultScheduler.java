package com.desafio.sicred.votacao.config.infra.scheduler;

import com.desafio.sicred.votacao.application.core.resultado.usecases.PublishClosedSessionResultsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SessionResultScheduler {

    private final PublishClosedSessionResultsUseCase publishClosedSessionResultsUseCase;

    @Scheduled(fixedDelayString = "${scheduler.session-result.fixed-delay-ms:30000}")
    public void publishClosedSessionResults() {
        log.debug("Verificando sessões encerradas para publicação de resultado...");
        publishClosedSessionResultsUseCase.execute();
    }
}
