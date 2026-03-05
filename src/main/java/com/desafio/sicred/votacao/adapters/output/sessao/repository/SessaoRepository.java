package com.desafio.sicred.votacao.adapters.output.sessao.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.desafio.sicred.votacao.adapters.output.sessao.entities.SessaoEntity;

public interface SessaoRepository {
    SessaoEntity save(SessaoEntity entity);

    Optional<SessaoEntity> findByPautaIdAndFechamentoAfter(UUID pautaId);

    Optional<SessaoEntity> findById(UUID id);

    List<SessaoEntity> findByPautaId(UUID pautaId);

    List<SessaoEntity> findClosedAndUnpublished();

    void markResultadoPublicado(UUID id);
}
