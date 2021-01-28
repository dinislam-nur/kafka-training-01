package ru.dininslam.client.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.dininslam.client.dto.Request;
import ru.dininslam.client.dto.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ReplyingKafkaTemplate<Long, Request, Response> replyingTemplate(
            ProducerFactory<Long, Request> producerFactory,
            ConcurrentMessageListenerContainer<Long, Response> listenerContainer
    ) {
        return new ReplyingKafkaTemplate<>(producerFactory, listenerContainer);
    }

    @Bean
    public ConcurrentMessageListenerContainer<Long, Response> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<Long, Response> containerFactory
    ) {
        final TopicPartitionOffset topicPartitionOffset = new TopicPartitionOffset("response", 1);
        final ConcurrentMessageListenerContainer<Long, Response> container = containerFactory.createContainer(topicPartitionOffset);
        container.getContainerProperties().setGroupId(UUID.randomUUID().toString());
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        container.getContainerProperties().setKafkaConsumerProperties(properties);
        container.setAutoStartup(false);
        return container;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ConsumerFactory<Long, Response> consumerFactory(KafkaProperties properties) {
        final JsonDeserializer<Response> valueDeserializer = new JsonDeserializer<>(Response.class, objectMapper());
        return new DefaultKafkaConsumerFactory<>(
                properties.buildConsumerProperties(),
                new LongDeserializer(),
                valueDeserializer
        );
    }

    @Bean
    public ProducerFactory<Long, Request> producerFactory(KafkaProperties properties) {
        final JsonSerializer<Request> valueSerializer = new JsonSerializer<>(objectMapper());
        return new DefaultKafkaProducerFactory<>(
                properties.buildProducerProperties(),
                new LongSerializer(),
                valueSerializer
        );
    }

}
