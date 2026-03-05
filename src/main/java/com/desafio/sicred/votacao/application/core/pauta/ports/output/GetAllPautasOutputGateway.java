package com.desafio.sicred.votacao.application.core.pauta.ports.output;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

import java.util.List;

public interface GetAllPautasOutputGateway {
    List<Pauta> getAll();
}
