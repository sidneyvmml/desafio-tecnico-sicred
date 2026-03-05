package com.desafio.sicred.votacao.adapters.output.pauta.repository;

import com.desafio.sicred.votacao.adapters.output.pauta.entities.PautaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public List<PautaEntity> list() {
        return jpaRepository.findAll();
    }
}
