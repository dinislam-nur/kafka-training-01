package ru.dininslam.client.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@Component
//@RequiredArgsConstructor
//public class Producer {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public void send(String message) {
//        kafkaTemplate.send(new Message<String>() {
//            @Override
//            public String getPayload() {
//                return message;
//            }
//
//            @Override
//            public MessageHeaders getHeaders() {
//                final Map<String, Object> headers = new HashMap<>();
//                headers.put(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString());
//                return new MessageHeaders(headers);
//            }
//        });
//    }
//}
