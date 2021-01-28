package ru.dininslam.server.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.lang.NonNullApi;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import ru.dininslam.server.dto.Response;
import ru.dininslam.server.dto.ServerMessageConverter;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<Long, Response> kafkaTemplate;
    private final ServerMessageConverter converter;

    public void send(Response response, byte[] correlationId) {
        kafkaTemplate.send(new Message<String>() {

            @Override
            public String getPayload() {
                return converter.toMessage(response);
            }

            @Override
            public MessageHeaders getHeaders() {
                final Map<String, Object> headers = new HashMap<>();
                headers.put(KafkaHeaders.CORRELATION_ID, correlationId);
                headers.put(KafkaHeaders.TOPIC, "main");
                headers.put(KafkaHeaders.PARTITION_ID, 1);
                return new MessageHeaders(headers);
            }
        });
    }
}
