package com.desafio.sicred.votacao.application.core.pauta.usecases;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
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
@DisplayName("GetPautaByIdUseCase")
class GetPautaByIdUseCaseTest {

    @Mock
    private GetPautaByIdOutputGateway outputGateway;

    private GetPautaByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetPautaByIdUseCase(outputGateway);
    }

    @Test
    @DisplayName("deve retornar pauta quando encontrada")
    void deveRetornarPautaQuandoEncontrada() {
        UUID id = UUID.randomUUID();
        var pauta = new Pauta(id, "Titulo", "Descricao", LocalDateTime.now());
        when(outputGateway.getById(id)).thenReturn(Optional.of(pauta));

        var result = useCase.getById(id);

        assertThat(result).isEqualTo(pauta);
        verify(outputGateway).getById(id);
    }

    @Test
    @DisplayName("deve lançar PautaNotFoundException quando não encontrada")
    void deveLancarExcecaoQuandoPautaNaoEncontrada() {
        UUID id = UUID.randomUUID();
        when(outputGateway.getById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getById(id))
                .isInstanceOf(PautaNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}
