package ru.dininslam.server.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.dininslam.server.dto.Request;
import ru.dininslam.server.dto.ServerMessageConverter;
import ru.dininslam.server.kafka.excpetion_handler.ExceptionHandler;
import ru.dininslam.server.service.RequestHandler;

import javax.validation.Valid;

@Component
@RequiredArgsConstructor
public class Consumer {

    private final RequestHandler requestHandler;
    private final ServerMessageConverter converter;
    private final Producer producer;
    private final ExceptionHandler exceptionHandler;

    @KafkaListener(id = "server",
            topicPartitions = @TopicPartition(
                    topic = "main",
                    partitions = "0"
            )
    )
    public void receive(String message, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId) {
        System.out.println("Received message: " + message);
        exceptionHandler.handle(
                () -> {
                    final Request request = converter.fromMessage(message);
                    producer.send(
                            requestHandler.processRequest(request),
                            correlationId
                    );
                },
                correlationId
        );
    }
}
