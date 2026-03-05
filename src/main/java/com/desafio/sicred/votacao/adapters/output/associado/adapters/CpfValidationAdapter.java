package com.desafio.sicred.votacao.adapters.output.associado.adapters;

import com.desafio.sicred.votacao.adapters.output.associado.feign.CpfStatusResponse;
import com.desafio.sicred.votacao.adapters.output.associado.feign.CpfValidationFeignClient;
import com.desafio.sicred.votacao.adapters.output.associado.validation.CpfValidator;
import com.desafio.sicred.votacao.application.core.associado.exceptions.CpfInvalidoException;
import com.desafio.sicred.votacao.application.core.associado.ports.output.CpfValidationOutputGateway;
import feign.FeignException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CpfValidationAdapter implements CpfValidationOutputGateway {

    private final CpfValidationFeignClient feignClient;

    @Override
    public boolean isEligible(String cpf) {
        try {
            log.info("Validando elegibilidade via API externa: {}", maskCpf(cpf));
            CpfStatusResponse response = feignClient.getUserStatus(cpf);
            log.info("CPF {} — status externo: {}", maskCpf(cpf), response.status());
            return response.isEligible();

        } catch (FeignException.NotFound ex) {
            if (isApiUnavailable(ex)) {
                return fallbackToLocalValidation(cpf, ex);
            }
            log.warn("CPF não encontrado na base de validação externa: {}", maskCpf(cpf));
            throw new CpfInvalidoException("CPF inválido ou não encontrado: " + cpf);

        } catch (RetryableException ex) {
            return fallbackToLocalValidation(cpf, ex);

        } catch (FeignException ex) {
            return fallbackToLocalValidation(cpf, ex);
        }
    }

    private boolean isApiUnavailable(FeignException ex) {
        String body = ex.contentUTF8();
        return body == null || body.isBlank() || body.contains("<html") || body.contains("<!DOCTYPE");
    }

    private boolean fallbackToLocalValidation(String cpf, Exception cause) {
        log.warn("API de validação de CPF indisponível ({}). Aplicando validação local para: {}",
                cause.getMessage(), maskCpf(cpf));

        if (!CpfValidator.isValid(cpf)) {
            log.warn("CPF reprovado na validação local: {}", maskCpf(cpf));
            throw new CpfInvalidoException("CPF inválido: " + cpf);
        }

        log.warn("API de CPF indisponível — CPF {} aprovado localmente (matematicamente válido). "
                + "Elegibilidade não confirmada pela fonte externa.", maskCpf(cpf));
        return true;
    }

    private String maskCpf(String cpf) {
        if (cpf == null || cpf.length() < 6) return "***";
        return cpf.substring(0, 3) + "***" + cpf.substring(cpf.length() - 2);
    }
}

