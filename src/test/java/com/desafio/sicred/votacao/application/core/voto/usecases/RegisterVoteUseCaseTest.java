package com.desafio.sicred.votacao.application.core.voto.usecases;

import com.desafio.sicred.votacao.application.core.associado.exceptions.AssociadoNaoElegivelException;
import com.desafio.sicred.votacao.application.core.associado.ports.output.CpfValidationOutputGateway;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessionEndedException;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessaoNotFoundException;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindStartedSessionOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.domain.RegisterVoteCommand;
import com.desafio.sicred.votacao.application.core.voto.domain.Vote;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import com.desafio.sicred.votacao.application.core.voto.exceptions.VotoDuplicadoException;
import com.desafio.sicred.votacao.application.core.voto.ports.output.SaveVoteOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterVoteUseCase")
class RegisterVoteUseCaseTest {

    @Mock
    private GetPautaByIdOutputGateway getPautaByIdOutputGateway;
    @Mock
    private FindStartedSessionOutputGateway findStartedSessionOutputGateway;
    @Mock
    private CpfValidationOutputGateway cpfValidationOutputGateway;
    @Mock
    private SaveVoteOutputGateway saveVoteOutputGateway;

    private RegisterVoteUseCase useCase;

    private final UUID pautaId = UUID.randomUUID();
    private final String cpf = "12345678901";
    private Pauta pauta;
    private Session sessaoAberta;

    @BeforeEach
    void setUp() {
        useCase = new RegisterVoteUseCase(
                getPautaByIdOutputGateway,
                findStartedSessionOutputGateway,
                cpfValidationOutputGateway,
                saveVoteOutputGateway);
        pauta = new Pauta(pautaId, "Titulo", "Desc", LocalDateTime.now());
        sessaoAberta = Session.builder()
                .id(UUID.randomUUID())
                .pautaId(pautaId)
                .abertura(LocalDateTime.now().minusMinutes(1))
                .fechamento(LocalDateTime.now().plusMinutes(30))
                .resultadoPublicado(false)
                .build();
    }

    @Test
    @DisplayName("deve registrar voto com sucesso")
    void deveRegistrarVotoComSucesso() {
        var command = new RegisterVoteCommand(pautaId, cpf, VoteOption.SIM);
        var vote = Vote.create(pautaId, cpf, VoteOption.SIM);

        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId)).thenReturn(Optional.of(sessaoAberta));
        when(cpfValidationOutputGateway.isEligible(cpf)).thenReturn(true);
        when(saveVoteOutputGateway.save(any())).thenReturn(vote);

        var result = useCase.register(command);

        assertThat(result.getPautaId()).isEqualTo(pautaId);
        assertThat(result.getCpf()).isEqualTo(cpf);
        assertThat(result.getOption()).isEqualTo(VoteOption.SIM);
        verify(saveVoteOutputGateway).save(any(Vote.class));
    }

    @Test
    @DisplayName("deve lançar PautaNotFoundException quando pauta não existe")
    void deveLancarExcecaoQuandoPautaNaoExiste() {
        var command = new RegisterVoteCommand(pautaId, cpf, VoteOption.SIM);
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(PautaNotFoundException.class);

        verifyNoInteractions(saveVoteOutputGateway);
    }

    @Test
    @DisplayName("deve lançar SessaoNotFoundException quando não há sessão aberta")
    void deveLancarExcecaoQuandoNaoHaSessaoAberta() {
        var command = new RegisterVoteCommand(pautaId, cpf, VoteOption.NAO);
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(SessaoNotFoundException.class)
                .hasMessageContaining(pautaId.toString());

        verifyNoInteractions(saveVoteOutputGateway);
    }

    @Test
    @DisplayName("deve lançar SessionEndedException quando sessão está encerrada")
    void deveLancarExcecaoQuandoSessaoEncerrada() {
        var command = new RegisterVoteCommand(pautaId, cpf, VoteOption.SIM);
        var sessaoEncerrada = Session.builder()
                .id(UUID.randomUUID())
                .pautaId(pautaId)
                .abertura(LocalDateTime.now().minusMinutes(10))
                .fechamento(LocalDateTime.now().minusMinutes(1))
                .resultadoPublicado(false)
                .build();

        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId))
                .thenReturn(Optional.of(sessaoEncerrada));

        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(SessionEndedException.class);

        verifyNoInteractions(saveVoteOutputGateway);
    }

    @Test
    @DisplayName("deve lançar AssociadoNaoElegivelException quando associado não pode votar")
    void deveLancarExcecaoQuandoAssociadoNaoElegivel() {
        var command = new RegisterVoteCommand(pautaId, cpf, VoteOption.SIM);

        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId)).thenReturn(Optional.of(sessaoAberta));
        when(cpfValidationOutputGateway.isEligible(cpf)).thenReturn(false);

        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(AssociadoNaoElegivelException.class)
                .hasMessageContaining(cpf);

        verifyNoInteractions(saveVoteOutputGateway);
    }

    @Test
    @DisplayName("deve lançar VotoDuplicadoException em violação de constraint no banco")
    void deveLancarExcecaoEmVotoDuplicado() {
        var command = new RegisterVoteCommand(pautaId, cpf, VoteOption.SIM);

        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(findStartedSessionOutputGateway.buscarSessaoAbertaPorPauta(pautaId)).thenReturn(Optional.of(sessaoAberta));
        when(cpfValidationOutputGateway.isEligible(cpf)).thenReturn(true);
        when(saveVoteOutputGateway.save(any())).thenThrow(new DataIntegrityViolationException("uk_voto_pauta_cpf"));

        assertThatThrownBy(() -> useCase.register(command))
                .isInstanceOf(VotoDuplicadoException.class)
                .hasMessageContaining(cpf);
    }
}
