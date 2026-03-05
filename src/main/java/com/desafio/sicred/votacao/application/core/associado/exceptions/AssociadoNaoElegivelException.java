package com.desafio.sicred.votacao.application.core.associado.exceptions;

import com.desafio.sicred.votacao.application.core.exceptions.DomainException;

public class AssociadoNaoElegivelException extends DomainException {
    public AssociadoNaoElegivelException(String message) {
        super(message);
    }
}
