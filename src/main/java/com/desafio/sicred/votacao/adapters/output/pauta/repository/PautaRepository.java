package com.desafio.sicred.votacao.adapters.output.pauta.repository;

import com.desafio.sicred.votacao.adapters.output.pauta.entities.PautaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PautaRepository {
    PautaEntity save(PautaEntity entity);

    Optional<PautaEntity> getById(UUID id);

    Page<PautaEntity> list(Pageable pageable);
}
