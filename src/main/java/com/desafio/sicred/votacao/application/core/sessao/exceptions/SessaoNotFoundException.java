package com.desafio.sicred.votacao.application.core.sessao.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class SessaoNotFoundException extends DomainException {
    public SessaoNotFoundException(String message) {
        super(message);
    }
}
