package com.desafio.sicred.votacao.adapters.output.voto.adapters;

import com.desafio.sicred.votacao.adapters.output.voto.entities.VoteEntity;
import com.desafio.sicred.votacao.adapters.output.voto.repository.VoteRepository;
import com.desafio.sicred.votacao.application.core.voto.domain.Vote;
import com.desafio.sicred.votacao.application.core.voto.ports.output.SaveVoteOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveVoteAdapter implements SaveVoteOutputGateway {

    private final VoteRepository voteRepository;

    @Override
    public Vote save(Vote vote) {
        VoteEntity saved = voteRepository.save(VoteEntity.fromDomain(vote));
        log.info("Voto registrado: id={}", saved.getId());
        return saved.toDomain();
    }
}
