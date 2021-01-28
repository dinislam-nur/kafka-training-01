package ru.dininslam.client.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.dininslam.client.dto.ClientMessageConverter;
import ru.dininslam.client.dto.Request;
import ru.dininslam.client.dto.Response;
import ru.dininslam.client.dto.enums.Operation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class KafkaBillService {

    @Value("${sourceId}")
    private String sourceId;

    @Value("${targetId}")
    private String targetId;

    @Value("${value}")
    private String value;

    @Value("${operation}")
    private String operation;

    private final ReplyingKafkaTemplate<Long, Request, Response>  replyingKafkaTemplate;

    public void sendAndReceive() {
        try {
            final long sourceId = Long.parseLong(this.sourceId);
            final Request request = Request.builder()
                    .sourceId(sourceId)
                    .targetId(Long.parseLong(targetId))
                    .operation(Operation.valueOf(operation))
                    .value(Long.parseLong(value))
                    .build();
            replyingKafkaTemplate.setSharedReplyTopic(true);
            final ProducerRecord<Long, Request> record = new ProducerRecord<>("response", 0, sourceId, request);
            final RequestReplyFuture<Long, Request, Response> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
            final SendResult<Long, Request> sentResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
            System.out.println("Success sent: " + sentResult.getRecordMetadata());
            final ConsumerRecord<Long, Response> consumedResult = replyFuture.get(10, TimeUnit.SECONDS);
            final Response response = consumedResult.value();
            System.out.println(response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("Failure to send");
        } catch (TimeoutException e) {
            e.printStackTrace();
            sendAndReceive();
        }

    }

}
