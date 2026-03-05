package com.desafio.sicred.votacao.application.core.sessao.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class SessaoEncerradaException extends DomainException {
    public SessaoEncerradaException(String message) {
        super(message);
    }
}
