package com.desafio.sicred.votacao.application.core.associado.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class CpfInvalidoException extends DomainException {
    public CpfInvalidoException(String message) {
        super(message);
    }
}
