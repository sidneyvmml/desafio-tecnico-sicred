package com.desafio.sicred.votacao.application.core.pauta.ports.input;

import com.desafio.sicred.votacao.application.core.pauta.domain.Pauta;

import java.util.List;

public interface GetAllPautasInputGateway {
    List<Pauta> getAll();
}
