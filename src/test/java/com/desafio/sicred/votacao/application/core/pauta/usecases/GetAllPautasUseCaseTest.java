package com.desafio.sicred.votacao.application.core.pauta.usecases;

import com.desafio.sicred.votacao.application.core.common.PageRequest;
import com.desafio.sicred.votacao.application.core.common.PageResult;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetAllPautasOutputGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAllPautasUseCase")
class GetAllPautasUseCaseTest {

    @Mock
    private GetAllPautasOutputGateway outputGateway;

    private GetAllPautasUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllPautasUseCase(outputGateway);
    }

    @Test
    @DisplayName("deve retornar página de pautas do gateway")
    void deveRetornarPaginaDePautas() {
        var pageRequest = PageRequest.of(0, 10);
        var pauta = new Pauta(UUID.randomUUID(), "Titulo", "Desc", LocalDateTime.now());
        var pageResult = PageResult.of(List.of(pauta), 0, 10, 1L);
        when(outputGateway.getAll(pageRequest)).thenReturn(pageResult);

        var result = useCase.getAll(pageRequest);

        assertThat(result.content()).hasSize(1);
        assertThat(result.totalElements()).isEqualTo(1);
        verify(outputGateway).getAll(pageRequest);
    }

    @Test
    @DisplayName("deve retornar página vazia quando não há pautas")
    void deveRetornarPaginaVaziaQuandoNaoHaPautas() {
        var pageRequest = PageRequest.of(0, 10);
        var pageResult = PageResult.<Pauta>of(List.of(), 0, 10, 0L);
        when(outputGateway.getAll(pageRequest)).thenReturn(pageResult);

        var result = useCase.getAll(pageRequest);

        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }
}
