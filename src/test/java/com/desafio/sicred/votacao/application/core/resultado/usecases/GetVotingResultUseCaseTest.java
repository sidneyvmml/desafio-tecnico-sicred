package com.desafio.sicred.votacao.application.core.resultado.usecases;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.domain.ResultWinner;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.CountVotesOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetVotingResultUseCase")
class GetVotingResultUseCaseTest {

    @Mock
    private GetPautaByIdOutputGateway getPautaByIdOutputGateway;
    @Mock
    private CountVotesOutputGateway countVotesOutputGateway;

    private GetVotingResultUseCase useCase;

    private final UUID pautaId = UUID.randomUUID();
    private Pauta pauta;

    @BeforeEach
    void setUp() {
        useCase = new GetVotingResultUseCase(getPautaByIdOutputGateway, countVotesOutputGateway);
        pauta = new Pauta(pautaId, "Titulo", "Desc", LocalDateTime.now());
    }

    @Test
    @DisplayName("deve retornar resultado com vencedor SIM")
    void deveRetornarResultadoComVencedorSIM() {
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.SIM)).thenReturn(7L);
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.NAO)).thenReturn(3L);

        var result = useCase.getResult(pautaId);

        assertThat(result.getPautaId()).isEqualTo(pautaId);
        assertThat(result.getSimCount()).isEqualTo(7);
        assertThat(result.getNaoCount()).isEqualTo(3);
        assertThat(result.getTotal()).isEqualTo(10);
        assertThat(result.getWinner()).isEqualTo(ResultWinner.SIM);
    }

    @Test
    @DisplayName("deve retornar resultado EMPATE quando votos iguais")
    void deveRetornarEmpate() {
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.SIM)).thenReturn(5L);
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.NAO)).thenReturn(5L);

        var result = useCase.getResult(pautaId);

        assertThat(result.getWinner()).isEqualTo(ResultWinner.EMPATE);
    }

    @Test
    @DisplayName("deve retornar resultado sem votos")
    void deveRetornarResultadoSemVotos() {
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.SIM)).thenReturn(0L);
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.NAO)).thenReturn(0L);

        var result = useCase.getResult(pautaId);

        assertThat(result.getTotal()).isZero();
        assertThat(result.getWinner()).isEqualTo(ResultWinner.EMPATE);
    }

    @Test
    @DisplayName("deve lançar PautaNotFoundException quando pauta não existe")
    void deveLancarExcecaoQuandoPautaNaoExiste() {
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getResult(pautaId))
                .isInstanceOf(PautaNotFoundException.class)
                .hasMessageContaining(pautaId.toString());
    }

    @Test
    @DisplayName("deve consultar SIM e NAO separadamente")
    void deveConsultarSimENaoSeparadamente() {
        when(getPautaByIdOutputGateway.getById(pautaId)).thenReturn(Optional.of(pauta));
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.SIM)).thenReturn(2L);
        when(countVotesOutputGateway.countByOption(pautaId, VoteOption.NAO)).thenReturn(9L);

        useCase.getResult(pautaId);

        verify(countVotesOutputGateway).countByOption(pautaId, VoteOption.SIM);
        verify(countVotesOutputGateway).countByOption(pautaId, VoteOption.NAO);
    }
}
