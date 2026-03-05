package com.desafio.sicred.votacao.config.infra.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.voting-result}")
    private String votingResultTopic;

    @Bean
    public NewTopic votingResultTopic() {
        return TopicBuilder.name(votingResultTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
