package com.desafio.sicred.votacao.application.core.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("PageRequest")
class PageRequestTest {

    @Test
    @DisplayName("of() deve criar com valores válidos")
    void deveCriarComValoresValidos() {
        var req = PageRequest.of(2, 50);
        assertThat(req.page()).isEqualTo(2);
        assertThat(req.size()).isEqualTo(50);
    }

    @Test
    @DisplayName("ofDefault() deve usar page=0 e size=20")
    void ofDefaultDeveUsarValoresPadrao() {
        var req = PageRequest.ofDefault();
        assertThat(req.page()).isZero();
        assertThat(req.size()).isEqualTo(20);
    }

    @Test
    @DisplayName("deve lançar exceção quando page for negativo")
    void deveLancarExcecaoQuandoPageNegativo() {
        assertThatThrownBy(() -> PageRequest.of(-1, 10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deve lançar exceção quando size for zero")
    void deveLancarExcecaoQuandoSizeZero() {
        assertThatThrownBy(() -> PageRequest.of(0, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("deve lançar exceção quando size exceder 100")
    void deveLancarExcecaoQuandoSizeExcederLimite() {
        assertThatThrownBy(() -> PageRequest.of(0, 101))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
