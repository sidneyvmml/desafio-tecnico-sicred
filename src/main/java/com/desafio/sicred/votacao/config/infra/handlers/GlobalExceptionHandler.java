package com.desafio.sicred.votacao.config.infra.handlers;

import com.desafio.sicred.votacao.application.core.associado.exceptions.AssociadoNaoElegivelException;
import com.desafio.sicred.votacao.application.core.associado.exceptions.CpfInvalidoException;
import com.desafio.sicred.votacao.application.core.pauta.exceptions.PautaNotFoundException;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessaoEncerradaException;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessaoJaAbertaException;
import com.desafio.sicred.votacao.application.core.sessao.exceptions.SessaoNotFoundException;
import com.desafio.sicred.votacao.application.core.voto.exceptions.VotoDuplicadoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PautaNotFoundException.class)
    public ProblemDetail handlePautaNaoEncontrada(PautaNotFoundException ex) {
        log.warn("Pauta não encontrada: {}", ex.getMessage());
        return problem(HttpStatus.NOT_FOUND, "pauta-nao-encontrada", "Pauta não encontrada", ex.getMessage());
    }

    @ExceptionHandler(SessaoNotFoundException.class)
    public ProblemDetail handleSessaoNaoEncontrada(SessaoNotFoundException ex) {
        log.warn("Sessão não encontrada: {}", ex.getMessage());
        return problem(HttpStatus.NOT_FOUND, "sessao-nao-encontrada", "Sessão não encontrada", ex.getMessage());
    }

    @ExceptionHandler(SessaoJaAbertaException.class)
    public ProblemDetail handleSessaoJaAberta(SessaoJaAbertaException ex) {
        log.warn("Sessão já aberta: {}", ex.getMessage());
        return problem(HttpStatus.CONFLICT, "sessao-ja-aberta", "Sessão já aberta", ex.getMessage());
    }

    @ExceptionHandler(SessaoEncerradaException.class)
    public ProblemDetail handleSessaoEncerrada(SessaoEncerradaException ex) {
        log.warn("Sessão encerrada: {}", ex.getMessage());
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "sessao-encerrada", "Sessão encerrada", ex.getMessage());
    }

    @ExceptionHandler(VotoDuplicadoException.class)
    public ProblemDetail handleVotoDuplicado(VotoDuplicadoException ex) {
        log.warn("Voto duplicado: {}", ex.getMessage());
        return problem(HttpStatus.CONFLICT, "voto-duplicado", "Voto duplicado", ex.getMessage());
    }

    @ExceptionHandler(AssociadoNaoElegivelException.class)
    public ProblemDetail handleAssociadoNaoElegivel(AssociadoNaoElegivelException ex) {
        log.warn("Associado inelegível: {}", ex.getMessage());
        return problem(HttpStatus.FORBIDDEN, "associado-nao-elegivel", "Associado não elegível", ex.getMessage());
    }

    @ExceptionHandler(CpfInvalidoException.class)
    public ProblemDetail handleCpfInvalido(CpfInvalidoException ex) {
        log.warn("CPF inválido: {}", ex.getMessage());
        return problem(HttpStatus.UNPROCESSABLE_ENTITY, "cpf-invalido", "CPF inválido", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Erro de validação: {}", detail);
        return problem(HttpStatus.BAD_REQUEST, "requisicao-invalida", "Requisição inválida", detail);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Erro inesperado: {}", ex.getMessage(), ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "erro-interno", "Erro interno", "Ocorreu um erro inesperado.");
    }

    private ProblemDetail problem(HttpStatus status, String type, String title, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setType(URI.create("https://sicredi.com.br/errors/" + type));
        pd.setTitle(title);
        return pd;
    }
}

