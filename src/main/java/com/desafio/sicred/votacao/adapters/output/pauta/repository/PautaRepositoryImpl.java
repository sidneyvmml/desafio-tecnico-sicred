package com.desafio.sicred.votacao.adapters.output.pauta.repository;

import com.desafio.sicred.votacao.adapters.output.pauta.entities.PautaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PautaRepositoryImpl implements PautaRepository {

    private final PautaJpaRepository jpaRepository;

    @Override
    public PautaEntity save(PautaEntity entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<PautaEntity> getById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Page<PautaEntity> list(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }
}
