package com.desafio.sicred.votacao.adapters.output.resultado.adapters;

import com.desafio.sicred.votacao.adapters.output.resultado.events.VotingResultEvent;
import com.desafio.sicred.votacao.application.core.resultado.domain.VotingResult;
import com.desafio.sicred.votacao.application.core.resultado.ports.output.PublishVotingResultOutputGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotingResultKafkaPublisher implements PublishVotingResultOutputGateway {

    private final KafkaTemplate<String, VotingResultEvent> kafkaTemplate;

    @Value("${kafka.topics.voting-result}")
    private String topic;

    @Override
    public void publish(VotingResult result) {
        VotingResultEvent event = VotingResultEvent.from(result);
        kafkaTemplate.send(topic, result.getPautaId().toString(), event)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("Falha ao enviar resultado para kafka: pautaId={}", result.getPautaId(), ex);
                    } else {
                        log.info("Resultado enviado ao kafka: topic={}, pautaId={}, partition={}, offset={}",
                                topic, result.getPautaId(),
                                res.getRecordMetadata().partition(),
                                res.getRecordMetadata().offset());
                    }
                });
    }
}
