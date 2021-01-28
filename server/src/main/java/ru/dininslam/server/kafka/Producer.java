package ru.dininslam.server.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
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

    public void send(Response response, byte[] correlationId) {
        kafkaTemplate.send(new Message<Response>() {

            @Override
            public Response getPayload() {
                return response;
            }

            @Override
            public MessageHeaders getHeaders() {
                final Map<String, Object> headers = new HashMap<>();
                headers.put(KafkaHeaders.CORRELATION_ID, correlationId);
                headers.put(KafkaHeaders.TOPIC, "response");
                headers.put(KafkaHeaders.PARTITION_ID, 1);
                return new MessageHeaders(headers);
            }
        });
//        final ProducerRecord<String, String> record = new ProducerRecord<String, String>(
//                "response", 0, null, converter.toMessage(response), Arrays.asList(new Header() {
//            @Override
//            public String key() {
//                return KafkaHeaders.CORRELATION_ID;
//            }
//
//            @Override
//            public byte[] value() {
//                return correlationId;
//            }
//        })
//        );
//        kafkaTemplate.send(record);

    }
}
