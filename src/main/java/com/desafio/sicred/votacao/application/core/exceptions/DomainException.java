package com.desafio.sicred.votacao.application.core.exceptions;

public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}
