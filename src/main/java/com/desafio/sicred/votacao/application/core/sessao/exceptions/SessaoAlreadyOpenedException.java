package com.desafio.sicred.votacao.application.core.sessao.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class SessaoAlreadyOpenedException extends DomainException {
    public SessaoAlreadyOpenedException(String message) {
        super(message);
    }
}
