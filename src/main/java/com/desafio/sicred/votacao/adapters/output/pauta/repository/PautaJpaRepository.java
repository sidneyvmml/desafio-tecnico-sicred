package com.desafio.sicred.votacao.adapters.output.pauta.repository;

import com.desafio.sicred.votacao.adapters.output.pauta.entities.PautaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PautaJpaRepository extends JpaRepository<PautaEntity, UUID> {
}
