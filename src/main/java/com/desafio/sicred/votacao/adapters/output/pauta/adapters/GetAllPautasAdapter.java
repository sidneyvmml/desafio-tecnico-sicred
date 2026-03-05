package com.desafio.sicred.votacao.adapters.output.pauta.adapters;

import com.desafio.sicred.votacao.adapters.output.pauta.repository.PautaRepository;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetAllPautasOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAllPautasAdapter implements GetAllPautasOutputGateway {

    private final PautaRepository pautaRepository;

    @Override
    public List<Pauta> getAll() {
        log.info("Buscando todas as pautas");
        return pautaRepository.list().stream()
                .map(entity -> entity.toDomain())
                .toList();
    }
}
