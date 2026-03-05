package com.desafio.sicred.votacao.adapters.output.associado.adapters;

import com.desafio.sicred.votacao.adapters.output.associado.feign.CpfStatusResponse;
import com.desafio.sicred.votacao.adapters.output.associado.feign.CpfValidationFeignClient;
import com.desafio.sicred.votacao.application.core.associado.exceptions.CpfInvalidoException;
import com.desafio.sicred.votacao.application.core.associado.ports.output.CpfValidationOutputGateway;
import feign.FeignException;
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
            log.info("Validando elegibilidade do CPF: {}", maskCpf(cpf));
            CpfStatusResponse response = feignClient.getUserStatus(cpf);
            boolean eligible = response.isEligible();
            log.info("CPF {} — status: {}", maskCpf(cpf), response.status());
            return eligible;
        } catch (FeignException.NotFound ex) {
            log.warn("CPF não encontrado na base de validação: {}", maskCpf(cpf));
            throw new CpfInvalidoException("CPF não encontrado: " + cpf);
        } catch (FeignException ex) {
            log.error("Erro ao validar CPF {}: status={}", maskCpf(cpf), ex.status());
            throw new CpfInvalidoException("Não foi possível validar o CPF. Tente novamente.");
        }
    }

    private String maskCpf(String cpf) {
        if (cpf == null || cpf.length() < 6) return "***";
        return cpf.substring(0, 3) + "***" + cpf.substring(cpf.length() - 2);
    }
}
