package com.desafio.sicred.votacao.adapters.output.pauta.entities;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pauta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "criada_em", nullable = false)
    private LocalDateTime criadaEm;

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

    public static PautaEntity fromDomain(Pauta pauta) {
        return PautaEntity.builder()
                .id(pauta.getId())
                .titulo(pauta.getTitulo())
                .descricao(pauta.getDescricao())
                .criadaEm(pauta.getCriadaEm())
                .isNew(true)
                .build();
    }

    public Pauta toDomain() {
        return new Pauta(id, titulo, descricao, criadaEm);
    }
}
