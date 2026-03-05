package com.desafio.sicred.votacao.application.core.resultado.usecases;

import com.desafio.sicred.votacao.application.core.resultado.domain.ResultWinner;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.CountVotesOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.PublishVotingResultOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindClosedUnpublishedSessionsOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.MarkSessionResultPublishedOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PublishClosedSessionResultsUseCase")
class PublishClosedSessionResultsUseCaseTest {

    @Mock
    private FindClosedUnpublishedSessionsOutputGateway findClosedUnpublishedSessionsOutputGateway;
    @Mock
    private CountVotesOutputGateway countVotesOutputGateway;
    @Mock
    private PublishVotingResultOutputGateway publishVotingResultOutputGateway;
    @Mock
    private MarkSessionResultPublishedOutputGateway markSessionResultPublishedOutputGateway;

    private PublishClosedSessionResultsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new PublishClosedSessionResultsUseCase(
                findClosedUnpublishedSessionsOutputGateway,
                countVotesOutputGateway,
                publishVotingResultOutputGateway,
                markSessionResultPublishedOutputGateway);
    }

    @Test
    @DisplayName("não deve publicar nada quando não há sessões encerradas")
    void naoDevePublicarQuandoNaoHaSessoes() {
        when(findClosedUnpublishedSessionsOutputGateway.findClosedAndUnpublished())
                .thenReturn(List.of());

        useCase.execute();

        verifyNoInteractions(countVotesOutputGateway,
                publishVotingResultOutputGateway,
                markSessionResultPublishedOutputGateway);
    }

    @Test
    @DisplayName("deve publicar resultado e marcar sessão como publicada")
    void devePublicarResultadoEMarcarSessao() {
        var pautaId = UUID.randomUUID();
        var sessionId = UUID.randomUUID();
        var session = sessaoEncerrada(sessionId, pautaId);

        when(findClosedUnpublishedSessionsOutputGateway.findClosedAndUnpublished())
                .thenReturn(List.of(session));
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.SIM)).thenReturn(10L);
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.NAO)).thenReturn(4L);

        useCase.execute();

        var captor = ArgumentCaptor.forClass(
                com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult.class);
        verify(publishVotingResultOutputGateway).publish(captor.capture());
        assertThat(captor.getValue().getWinner()).isEqualTo(ResultWinner.SIM);
        assertThat(captor.getValue().getSimCount()).isEqualTo(10);

        verify(markSessionResultPublishedOutputGateway).markAsPublished(sessionId);
    }

    @Test
    @DisplayName("deve processar todas as sessões mesmo que uma falhe")
    void deveProcessarTodasAsSessoesMesmoComFalha() {
        var pautaId1 = UUID.randomUUID();
        var pautaId2 = UUID.randomUUID();
        var session1 = sessaoEncerrada(UUID.randomUUID(), pautaId1);
        var session2 = sessaoEncerrada(UUID.randomUUID(), pautaId2);

        when(findClosedUnpublishedSessionsOutputGateway.findClosedAndUnpublished())
                .thenReturn(List.of(session1, session2));

        // session1 falha ao publicar
        when(countVotesOutputGateway.countByOption(pautaId1, VoteOption.SIM)).thenReturn(1L);
        when(countVotesOutputGateway.countByOption(pautaId1, VoteOption.NAO)).thenReturn(0L);
        doThrow(new RuntimeException("Kafka indisponível"))
                .when(publishVotingResultOutputGateway).publish(argThat(r -> r.getPautaId().equals(pautaId1)));

        // session2 processa normalmente
        when(countVotesOutputGateway.countByOption(pautaId2, VoteOption.SIM)).thenReturn(3L);
        when(countVotesOutputGateway.countByOption(pautaId2, VoteOption.NAO)).thenReturn(2L);

        useCase.execute();

        // session2 deve ter sido marcada como publicada
        verify(markSessionResultPublishedOutputGateway).markAsPublished(session2.getId());
        // session1 NÃO deve ter sido marcada (publicação falhou)
        verify(markSessionResultPublishedOutputGateway, never()).markAsPublished(session1.getId());
    }

    @Test
    @DisplayName("deve publicar resultado EMPATE corretamente")
    void devePublicarResultadoEmpate() {
        var pautaId = UUID.randomUUID();
        var session = sessaoEncerrada(UUID.randomUUID(), pautaId);

        when(findClosedUnpublishedSessionsOutputGateway.findClosedAndUnpublished())
                .thenReturn(List.of(session));
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.SIM)).thenReturn(5L);
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.NAO)).thenReturn(5L);

        useCase.execute();

        var captor = ArgumentCaptor.forClass(
                com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult.class);
        verify(publishVotingResultOutputGateway).publish(captor.capture());
        assertThat(captor.getValue().getWinner()).isEqualTo(ResultWinner.EMPATE);
    }

    private Session sessaoEncerrada(UUID sessionId, UUID pautaId) {
        return Session.builder()
                .id(sessionId)
                .pautaId(pautaId)
                .abertura(LocalDateTime.now().minusMinutes(10))
                .fechamento(LocalDateTime.now().minusMinutes(1))
                .resultadoPublicado(false)
                .build();
    }
}
