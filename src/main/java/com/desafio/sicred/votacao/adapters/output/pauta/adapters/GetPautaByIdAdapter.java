package com.desafio.sicred.votacao.adapters.output.pauta.adapters;

import com.desafio.sicred.votacao.adapters.output.pauta.repository.PautaRepository;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPautaByIdAdapter implements GetPautaByIdOutputGateway {

    private final PautaRepository pautaRepository;

    @Override
    public Optional<Pauta> getById(UUID id) {
        log.info("Buscando pauta por id={}", id);
        return pautaRepository.getById(id).map(entity -> entity.toDomain());
    }
}
