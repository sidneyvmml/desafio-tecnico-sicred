package com.desafio.sicred.votacao.adapters.output.associado.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "cpf-validation",
        url = "${cpf-validation.url}"
)
public interface CpfValidationFeignClient {

    @GetMapping("/users/{cpf}")
    CpfStatusResponse getUserStatus(@PathVariable("cpf") String cpf);
}
