package com.desafio.sicred.votacao.adapters.output.pauta.adapters;

import com.desafio.sicred.votacao.adapters.output.pauta.entities.PautaEntity;
import com.desafio.sicred.votacao.adapters.output.pauta.repository.PautaRepository;
import com.desafio.sicred.votacao.application.core.pauta.domain.CreatePautaCommand;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.CreatePautaOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePautaAdapter implements CreatePautaOutputGateway {

    private final PautaRepository pautaRepository;

    @Override
    public Pauta create(CreatePautaCommand command) {
        log.info("Cadastrando pauta: titulo={}", command.titulo());
        PautaEntity entity = PautaEntity.fromDomain(Pauta.nova(command.titulo(), command.descricao()));
        Pauta salva = pautaRepository.save(entity).toDomain();
        log.info("Pauta cadastrada com id={}", salva.getId());
        return salva;
    }
}
