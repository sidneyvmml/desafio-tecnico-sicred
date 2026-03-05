package com.desafio.sicred.votacao.adapters.output.voto.entities;

import com.desafio.sicred.votacao.application.core.voto.domain.Vote;
import com.desafio.sicred.votacao.application.core.voto.domain.VoteOption;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "voto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "pauta_id", nullable = false)
    private UUID pautaId;

    @Column(name = "cpf_associado", nullable = false, length = 11)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "opcao", nullable = false, length = 3)
    private VoteOption option;

    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registeredAt;

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

    public static VoteEntity fromDomain(Vote vote) {
        return VoteEntity.builder()
                .id(vote.getId())
                .pautaId(vote.getPautaId())
                .cpf(vote.getCpf())
                .option(vote.getOption())
                .registeredAt(vote.getRegisteredAt())
                .isNew(true)
                .build();
    }

    public Vote toDomain() {
        return new Vote(id, pautaId, cpf, option, registeredAt);
    }
}
