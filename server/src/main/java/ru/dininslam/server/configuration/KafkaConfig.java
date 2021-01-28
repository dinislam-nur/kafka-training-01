package ru.dininslam.server.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.dininslam.server.dto.Request;
import ru.dininslam.server.dto.Response;
import ru.dininslam.server.kafka.Producer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic response() {
        return TopicBuilder
                .name("response")
                .partitions(2)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Request> containerFactory(ConsumerFactory<Long, Request> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<Long, Request> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setMessageConverter(new JsonMessageConverter());
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, Request> consumerFactory(KafkaProperties properties) {
//        final Map<String, Object> configurationProperties = new HashMap<>();
        final LongDeserializer keyDeserializer = new LongDeserializer();
        final JsonDeserializer<Request> valueDeserializer = new JsonDeserializer<>(Request.class, objectMapper());
//        configurationProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
//        configurationProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties(), keyDeserializer, new JsonDeserializer<>(Request.class, false));
    }

    @Bean
    public KafkaTemplate<Long, Response> kafkaTemplate(ProducerFactory<Long, Response> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<Long, Response> producerFactory(KafkaProperties kafkaProperties) {
//        final Map<String, Object> config = new HashMap<>();
        final LongSerializer keySerializer = new LongSerializer();
        final JsonSerializer<Response> valueSerializer = new JsonSerializer<>(objectMapper());
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(), keySerializer, valueSerializer);
    }
}
