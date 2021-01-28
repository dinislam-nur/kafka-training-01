package ru.dininslam.server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import ru.dininslam.server.dto.Response;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic main() {
        return TopicBuilder
                .name("main")
                .partitions(2)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public KafkaTemplate<Long, Response> kafkaTemplate(ProducerFactory<Long, Response> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
