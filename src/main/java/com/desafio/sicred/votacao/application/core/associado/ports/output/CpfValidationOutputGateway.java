package com.desafio.sicred.votacao.application.core.associado.ports.output;

public interface CpfValidationOutputGateway {
    boolean isEligible(String cpf);
}
