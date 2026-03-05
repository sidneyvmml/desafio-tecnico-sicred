package com.desafio.sicred.votacao.application.core.pauta.ports.output;

import com.desafio.sicred.votacao.application.core.common.PageRequest;
import com.desafio.sicred.votacao.application.core.common.PageResult;
import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

public interface GetAllPautasOutputGateway {
    PageResult<Pauta> getAll(PageRequest pageRequest);
}
