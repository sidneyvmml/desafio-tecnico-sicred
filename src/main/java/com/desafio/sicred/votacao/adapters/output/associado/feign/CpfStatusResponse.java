package com.desafio.sicred.votacao.adapters.output.associado.feign;

public record CpfStatusResponse(String status) {

    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    public boolean isEligible() {
        return ABLE_TO_VOTE.equalsIgnoreCase(status);
    }
}
