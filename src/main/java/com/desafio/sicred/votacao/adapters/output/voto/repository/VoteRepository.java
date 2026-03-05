package com.desafio.sicred.votacao.adapters.output.voto.repository;

import com.desafio.sicred.votacao.adapters.output.voto.entities.VoteEntity;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;

import java.util.List;
import java.util.UUID;

public interface VoteRepository {
    VoteEntity save(VoteEntity entity);
    List<VoteEntity> findByPautaId(UUID pautaId);
    long countByPautaIdAndOption(UUID pautaId, VoteOption option);
}
