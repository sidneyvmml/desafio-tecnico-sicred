package com.desafio.sicred.votacao.adapters.output.sessao.repository;

import com.desafio.sicred.votacao.adapters.output.sessao.entities.SessaoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SessaoRepositoryImpl implements SessaoRepository {

    private final SessaoJpaRepository jpaRepository;

    @Override
    public SessaoEntity save(SessaoEntity entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public Optional<SessaoEntity> findByPautaIdAndFechamentoAfter(UUID pautaId) {
        return jpaRepository.findSessaoAbertaByPautaId(pautaId, LocalDateTime.now());
    }

    @Override
    public Optional<SessaoEntity> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<SessaoEntity> findByPautaId(UUID pautaId) {
        return jpaRepository.findByPautaId(pautaId);
    }

    @Override
    public List<SessaoEntity> findClosedAndUnpublished() {
        return jpaRepository.findClosedAndUnpublished(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void markResultadoPublicado(UUID id) {
        jpaRepository.markResultadoPublicado(id);
    }
}
