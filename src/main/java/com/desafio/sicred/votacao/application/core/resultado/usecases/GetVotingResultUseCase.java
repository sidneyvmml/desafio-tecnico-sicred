package com.desafio.sicred.votacao.application.core.resultado.usecases;

import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult;
import com.desafio.sicred.votacao.application.core.resultado.ports.input.GetVotingResultInputGateway;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.CountVotesOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class GetVotingResultUseCase implements GetVotingResultInputGateway {

    private final GetPautaByIdOutputGateway getPautaByIdOutputGateway;
    private final CountVotesOutputGateway countVotesOutputGateway;

    @Override
    public VotingResult getResult(UUID pautaId) {
        getPautaByIdOutputGateway.getById(pautaId)
                .orElseThrow(() -> new PautaNotFoundException(
                        "Pauta não encontrada: " + pautaId));

        long sim = countVotesOutputGateway.countByOption(pautaId, VoteOption.SIM);
        long nao = countVotesOutputGateway.countByOption(pautaId, VoteOption.NAO);

        log.info("Resultado apurado: pautaId={}, SIM={}, NAO={}", pautaId, sim, nao);
        return VotingResult.of(pautaId, sim, nao);
    }
}
