package com.desafio.sicred.votacao.adapters.output.sessao.repository;

import com.desafio.sicred.votacao.adapters.output.sessao.entities.SessaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessaoJpaRepository extends JpaRepository<SessaoEntity, UUID> {

    @Query("SELECT s FROM SessaoEntity s WHERE s.pautaId = :pautaId AND s.fechamento > :agora")
    Optional<SessaoEntity> findSessaoAbertaByPautaId(
            @Param("pautaId") UUID pautaId,
            @Param("agora") LocalDateTime agora);

    List<SessaoEntity> findByPautaId(UUID pautaId);
}
