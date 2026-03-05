package com.desafio.sicred.votacao.adapters.output.sessao.entities;

import com.desafio.sicred.votacao.application.core.sessao.domain.Session;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "pauta_id", nullable = false)
    private UUID pautaId;

    @Column(nullable = false)
    private LocalDateTime abertura;

    @Column(nullable = false)
    private LocalDateTime fechamento;

    @Column(name = "resultado_publicado", nullable = false)
    private boolean resultadoPublicado;

    @Transient
    private boolean isNew = true;

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public static SessaoEntity fromDomain(Session session) {
        return SessaoEntity.builder()
                .id(session.getId())
                .pautaId(session.getPautaId())
                .abertura(session.getAbertura())
                .fechamento(session.getFechamento())
                .resultadoPublicado(session.isResultadoPublicado())
                .isNew(true)
                .build();
    }

    public Session toDomain() {
        return Session.builder()
                .id(id)
                .pautaId(pautaId)
                .abertura(abertura)
                .fechamento(fechamento)
                .resultadoPublicado(resultadoPublicado)
                .build();
    }
}
