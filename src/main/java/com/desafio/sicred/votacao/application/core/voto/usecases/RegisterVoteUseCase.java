package com.desafio.sicred.votacao.application.core.voto.usecases;

import com.desafio.sicred.votacao.application.core.associado.exceptions.AssociadoNaoElegivelException;
import com.desafio.sicred.votacao.application.core.associado.ports.output.CpfValidationOutputGateway;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.pauta.ports.output.GetPautaByIdOutputGateway;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessionEndedException;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessaoNotFoundException;
import com.desafio.sicred.votacao.application.core.sessao.ports.output.FindStartedSessionOutputGateway;
import com.desafio.sicred.votacao.application.core.voto.domain.RegisterVoteCommand;
import com.desafio.sicred.votacao.application.core.voto.domain.Vote;
import com.desafio.sicred.votacao.application.core.voto.exceptions.VotoDuplicadoException;
import com.desafio.sicred.votacao.application.core.voto.ports.input.RegisterVoteInputGateway;
import com.desafio.sicred.votacao.application.core.voto.ports.output.SaveVoteOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;

@Slf4j
@RequiredArgsConstructor
public class RegisterVoteUseCase implements RegisterVoteInputGateway {

    private final GetPautaByIdOutputGateway getPautaByIdOutputGateway;
    private final FindStartedSessionOutputGateway findOpenSessionOutputGateway;
    private final CpfValidationOutputGateway cpfValidationOutputGateway;
    private final SaveVoteOutputGateway saveVoteOutputGateway;

    @Override
    public Vote register(RegisterVoteCommand command) {
        getPautaByIdOutputGateway.getById(command.pautaId())
                .orElseThrow(() -> new PautaNotFoundException(
                        "Pauta não encontrada: " + command.pautaId()));

        var session = findOpenSessionOutputGateway.buscarSessaoAbertaPorPauta(command.pautaId())
                .orElseThrow(() -> new SessaoNotFoundException(
                        "Nenhuma sessão aberta para a pauta: " + command.pautaId()));

        if (!session.isAberta()) {
            throw new SessionEndedException(
                    "A sessão de votação está encerrada para a pauta: " + command.pautaId());
        }
        boolean eligible = cpfValidationOutputGateway.isEligible(command.cpf());
        if (!eligible) {
            throw new AssociadoNaoElegivelException(
                    "Associado não está elegível para votar: cpf=" + command.cpf());
        }
        try {
            Vote vote = Vote.create(command.pautaId(), command.cpf(), command.option());
            log.info("Registrando voto: pautaId={}, opcao={}", command.pautaId(), command.option());
            return saveVoteOutputGateway.save(vote);
        } catch (DataIntegrityViolationException ex) {
            throw new VotoDuplicadoException(
                    "Associado já votou nesta pauta: cpf=" + command.cpf());
        }
    }
}
