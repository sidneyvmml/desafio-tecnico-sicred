package com.desafio.sicred.votacao.application.core.resultado.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("VotingResult")
class VotingResultTest {

    @Test
    @DisplayName("vencedor deve ser SIM quando sim > nao")
    void vencedorDeveSerSIM() {
        var result = VotingResult.of(UUID.randomUUID(), 10, 3);

        assertThat(result.getWinner()).isEqualTo(ResultWinner.SIM);
        assertThat(result.getSimCount()).isEqualTo(10);
        assertThat(result.getNaoCount()).isEqualTo(3);
        assertThat(result.getTotal()).isEqualTo(13);
    }

    @Test
    @DisplayName("vencedor deve ser NAO quando nao > sim")
    void vencedorDeveSerNAO() {
        var result = VotingResult.of(UUID.randomUUID(), 2, 8);

        assertThat(result.getWinner()).isEqualTo(ResultWinner.NAO);
    }

    @Test
    @DisplayName("vencedor deve ser EMPATE quando sim == nao")
    void vencedorDeveSerEMPATE() {
        var result = VotingResult.of(UUID.randomUUID(), 5, 5);

        assertThat(result.getWinner()).isEqualTo(ResultWinner.EMPATE);
        assertThat(result.getTotal()).isEqualTo(10);
    }

    @Test
    @DisplayName("total deve ser zero quando sem votos")
    void totalDeveSerZeroSemVotos() {
        var result = VotingResult.of(UUID.randomUUID(), 0, 0);

        assertThat(result.getWinner()).isEqualTo(ResultWinner.EMPATE);
        assertThat(result.getTotal()).isZero();
    }
}
