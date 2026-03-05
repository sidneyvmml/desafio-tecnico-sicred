package com.desafio.sicred.votacao.adapters.output.voto.repository;

import com.desafio.sicred.votacao.adapters.output.voto.entities.VoteEntity;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VoteRepositoryImpl implements VoteRepository {

    private final VoteJpaRepository jpaRepository;

    @Override
    public VoteEntity save(VoteEntity entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public List<VoteEntity> findByPautaId(UUID pautaId) {
        return jpaRepository.findByPautaId(pautaId);
    }

    @Override
    public long countByPautaIdAndOption(UUID pautaId, VoteOption option) {
        return jpaRepository.countByPautaIdAndOption(pautaId, option);
    }
}
