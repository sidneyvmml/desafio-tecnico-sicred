package com.desafio.sicred.votacao.adapters.output.voto.repository;

import com.desafio.sicred.votacao.adapters.output.voto.entities.VoteEntity;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VoteJpaRepository extends JpaRepository<VoteEntity, UUID> {
    List<VoteEntity> findByPautaId(UUID pautaId);
    long countByPautaIdAndOption(UUID pautaId, VoteOption option);
}
