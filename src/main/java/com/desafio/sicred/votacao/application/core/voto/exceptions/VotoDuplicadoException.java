package com.desafio.sicred.votacao.application.core.voto.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class VotoDuplicadoException extends DomainException {
    public VotoDuplicadoException(String message) {
        super(message);
    }
}
