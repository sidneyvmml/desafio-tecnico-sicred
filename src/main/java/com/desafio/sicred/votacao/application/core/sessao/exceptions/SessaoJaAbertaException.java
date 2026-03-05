package com.desafio.sicred.votacao.application.core.sessao.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class SessaoJaAbertaException extends DomainException {
    public SessaoJaAbertaException(String message) {
        super(message);
    }
}
