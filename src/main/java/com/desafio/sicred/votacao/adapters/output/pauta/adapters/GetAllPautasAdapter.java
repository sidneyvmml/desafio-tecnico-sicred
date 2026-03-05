package com.desafio.sicred.votacao.adapters.output.pauta.adapters;

import com.desafio.sicred.votacao.adapters.output.pauta.repository.PautaRepository;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetAllPautasOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.desafio.sicred.votacao.application.core.common.PageRequest;
import com.desafio.sicred.votacao.application.core.common.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetAllPautasAdapter implements GetAllPautasOutputGateway {

    private final PautaRepository pautaRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResult<Pauta> getAll(com.desafio.sicred.votacao.application.core.common.PageRequest pageRequest) {
        log.info("Buscando pautas — page={}, size={}", pageRequest.page(), pageRequest.size());
        org.springframework.data.domain.PageRequest springPage =
                org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size(), Sort.by(Sort.Direction.DESC, "criadaEm"));
        Page<Pauta> page = pautaRepository.list(springPage).map(entity -> entity.toDomain());
        return PageResult.of(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
