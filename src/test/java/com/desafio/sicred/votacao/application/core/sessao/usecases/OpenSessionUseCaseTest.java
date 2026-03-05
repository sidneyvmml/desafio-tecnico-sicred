package com.desafio.sicred.votacao.application.core.sessao.usecases;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.domain.OpenSessionCommand;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessaoAlreadyOpenedException;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindStartedSessionOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.OpenSessionOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpenSessionUseCase")
class OpenSessionUseCaseTest {

    @Mock
    private GetPautaByIdOutputGateway getPautaByIdOutputGateway;
    @Mock
    private FindStartedSessionOutputGateway findStartedSessionOutputGateway;
    @Mock
    private OpenSessionOutputGateway openSessionOutputGateway;

    private OpenSessionUseCase useCase;

    private final UUID pautaId = UUID.randomUUID();
    private Pauta pauta;

    @BeforeEach
    void setUp() {
        useCase = new OpenSessionUseCase(
                getPautaByIdOutputGateway,
                findStartedSessionOutputGateway,
                openSessionOutputGateway);
        pauta = new Pauta(pautaId, "Titulo", "Desc", LocalDateTime.now());
    }

    @Test
    @DisplayName("deve abrir sessão com duração informada")
    void deveAbrirSessaoComDuracaoInformada() {
        var command = new OpenSessionCommand(pautaId, 5);
        var savedSession = Session.nova(pautaId, 5);

        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId)).thenReturn(Optional.empty());
        when(openSessionOutputGateway.save(any())).thenReturn(savedSession);

        var result = useCase.abrir(command);

        assertThat(result.getPautaId()).isEqualTo(pautaId);
        assertThat(result.isAberta()).isTrue();
        verify(openSessionOutputGateway).save(any(Session.class));
    }

    @Test
    @DisplayName("deve abrir sessão com duração padrão quando null")
    void deveAbrirSessaoComDuracaoPadrao() {
        var command = new OpenSessionCommand(pautaId, null);
        var savedSession = Session.nova(pautaId, null);

        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId)).thenReturn(Optional.empty());
        when(openSessionOutputGateway.save(any())).thenReturn(savedSession);

        var result = useCase.abrir(command);

        ArgumentCaptor<Session> captor = ArgumentCaptor.forClass(Session.class);
        verify(openSessionOutputGateway).save(captor.capture());
        assertThat(captor.getValue().getFechamento())
                .isAfter(captor.getValue().getAbertura());
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("deve lançar PautaNotFoundException quando pauta não existe")
    void deveLancarExcecaoQuandoPautaNaoExiste() {
        var command = new OpenSessionCommand(pautaId, 1);
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.abrir(command))
                .isInstanceOf(PautaNotFoundException.class)
                .hasMessageContaining(pautaId.toString());

        verify(openSessionOutputGateway, never()).save(any());
    }

    @Test
    @DisplayName("deve lançar SessaoAlreadyOpenedException quando já existe sessão aberta")
    void deveLancarExcecaoQuandoSessaoJaAberta() {
        var command = new OpenSessionCommand(pautaId, 1);
        var sessaoAberta = Session.nova(pautaId, 60);

        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId))
                .thenReturn(Optional.of(sessaoAberta));

        assertThatThrownBy(() -> useCase.abrir(command))
                .isInstanceOf(SessaoAlreadyOpenedException.class)
                .hasMessageContaining(pautaId.toString());

        verify(openSessionOutputGateway, never()).save(any());
    }
}
