package com.desafio.sicred.votacao.application.core.sessao.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class SessionEndedException extends DomainException {
    public SessionEndedException(String message) {
        super(message);
    }
}
