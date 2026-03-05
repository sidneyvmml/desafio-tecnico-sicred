package com.desafio.sicred.votacao.adapters.output.pauta.repository;

import com.desafio.sicred.votacao.adapters.output.pauta.entities.PautaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PautaRepository {
    PautaEntity save(PautaEntity entity);

    Optional<PautaEntity> getById(UUID id);

    List<PautaEntity> list();
}
