package com.desafio.sicred.votacao.application.core.sessao.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Session")
class SessionTest {

    @Test
    @DisplayName("nova() deve usar 1 minuto como duração padrão quando null")
    void deveUsarUmMinutoPorDefault() {
        var pautaId = UUID.randomUUID();
        var before = LocalDateTime.now();

        var session = Session.nova(pautaId, null);

        assertThat(session.getFechamento())
                .isAfterOrEqualTo(before.plusMinutes(1))
                .isBefore(before.plusMinutes(1).plusSeconds(2));
    }

    @Test
    @DisplayName("nova() deve usar 1 minuto quando duração for zero ou negativa")
    void deveUsarUmMinutoQuandoDuracaoForInvalida() {
        var session = Session.nova(UUID.randomUUID(), 0);

        assertThat(session.getFechamento())
                .isAfter(session.getAbertura());
        assertThat(session.getFechamento())
                .isBefore(session.getAbertura().plusMinutes(2));
    }

    @Test
    @DisplayName("nova() deve usar duração informada quando positiva")
    void deveUsarDuracaoInformada() {
        var session = Session.nova(UUID.randomUUID(), 30);

        assertThat(session.getFechamento())
                .isAfterOrEqualTo(session.getAbertura().plusMinutes(30));
    }

    @Test
    @DisplayName("isAberta() deve retornar true quando fechamento é no futuro")
    void deveEstarAbertaQuandoFechamentoNoFuturo() {
        var session = Session.builder()
                .id(UUID.randomUUID())
                .pautaId(UUID.randomUUID())
                .abertura(LocalDateTime.now().minusMinutes(1))
                .fechamento(LocalDateTime.now().plusMinutes(10))
                .resultadoPublicado(false)
                .build();

        assertThat(session.isAberta()).isTrue();
    }

    @Test
    @DisplayName("isAberta() deve retornar false quando fechamento é no passado")
    void deveEstarEncerradaQuandoFechamentoNoPassado() {
        var session = Session.builder()
                .id(UUID.randomUUID())
                .pautaId(UUID.randomUUID())
                .abertura(LocalDateTime.now().minusMinutes(10))
                .fechamento(LocalDateTime.now().minusMinutes(1))
                .resultadoPublicado(false)
                .build();

        assertThat(session.isAberta()).isFalse();
    }
}
