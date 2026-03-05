package com.desafio.sicred.votacao.application.core.pauta.usecases;

import com.desafio.sicred.votacao.application.core.pauta.domain.CreatePautaCommand;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.CreatePautaOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePautaUseCase")
class CreatePautaUseCaseTest {

    @Mock
    private CreatePautaOutputGateway outputGateway;

    private CreatePautaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreatePautaUseCase(outputGateway);
    }

    @Test
    @DisplayName("deve criar pauta delegando ao output gateway")
    void deveCriarPautaDelegandoAoGateway() {
        var command = new CreatePautaCommand("Titulo", "Descricao");
        var pauta = new Pauta(UUID.randomUUID(), "Titulo", "Descricao", LocalDateTime.now());
        when(outputGateway.create(command)).thenReturn(pauta);

        var result = useCase.create(command);

        assertThat(result).isEqualTo(pauta);
        verify(outputGateway).create(command);
    }
}
