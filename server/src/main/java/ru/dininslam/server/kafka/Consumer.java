package ru.dininslam.server.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.dininslam.server.dto.Request;
import ru.dininslam.server.dto.Response;
import ru.dininslam.server.dto.ServerMessageConverter;
import ru.dininslam.server.service.RequestHandler;

@Component
@RequiredArgsConstructor
public class Consumer {

    private final RequestHandler requestHandler;
    private final ServerMessageConverter converter;
    private final Producer producer;

    @KafkaListener(id = "server",
            topicPartitions = @TopicPartition(
                    topic = "response",
                    partitions = "0"
            )
    )
    public void receive(@Payload Request request) {
        System.out.println(request);
        final Response response = requestHandler.processRequest(request);
        producer.send(response, null);
    }
}
