package com.desafio.sicred.votacao.adapters.output.resultado.adapters;

import com.desafio.sicred.votacao.adapters.output.voto.repository.VoteRepository;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.CountVotesOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountVotesAdapter implements CountVotesOutputGateway {

    private final VoteRepository voteRepository;

    @Override
    public long countByOption(UUID pautaId, VoteOption option) {
        return voteRepository.countByPautaIdAndOption(pautaId, option);
    }
}
