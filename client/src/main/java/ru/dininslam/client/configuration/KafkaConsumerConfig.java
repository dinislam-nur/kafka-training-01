package ru.dininslam.client.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.TopicPartitionOffset;

import java.util.Properties;
import java.util.UUID;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate(
            ProducerFactory<String, String> producerFactory,
            ConcurrentMessageListenerContainer<String, String> listenerContainer
    ) {
        return new ReplyingKafkaTemplate<>(producerFactory, listenerContainer);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, String> containerFactory
    ) {
        final TopicPartitionOffset topicPartitionOffset = new TopicPartitionOffset("main", 1);
        final ConcurrentMessageListenerContainer<String, String> container = containerFactory.createContainer(topicPartitionOffset);
        container.getContainerProperties().setGroupId(UUID.randomUUID().toString());
        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        container.getContainerProperties().setKafkaConsumerProperties(properties);
        container.setAutoStartup(false);
        return container;
    }

}
