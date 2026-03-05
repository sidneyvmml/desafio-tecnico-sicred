package com.desafio.sicred.votacao.application.core.pauta.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class PautaNotFoundException extends DomainException {
    public PautaNotFoundException(String message) {
        super(message);
    }
}

