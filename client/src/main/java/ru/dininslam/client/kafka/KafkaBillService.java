package ru.dininslam.client.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import ru.dininslam.client.dto.ClientMessageConverter;
import ru.dininslam.client.dto.Request;
import ru.dininslam.client.dto.Response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
public class KafkaBillService {

    private final ReplyingKafkaTemplate<String, String, String>  replyingKafkaTemplate;
    private final ClientMessageConverter converter;

    public void sendAndReceive(Request request) {
        try {
            replyingKafkaTemplate.setSharedReplyTopic(true);
            final ProducerRecord<String, String> record = new ProducerRecord<>(
                    "main",
                    0,
                    String.valueOf(request.getSourceId()),
                    converter.toMessage(request));
            final RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
            final SendResult<String, String> sentResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
            System.out.println("Success sent: " + sentResult.getRecordMetadata());
            processResponse(replyFuture);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        } catch (ExecutionException e) {
            System.out.println("Failure execution request");
        } catch (TimeoutException e) {
            System.out.println("Failure by timeout");
            sendAndReceive(request);
        }

    }

    private void processResponse(RequestReplyFuture<String, String, String> future) {
        try {
            final ConsumerRecord<String, String> consumedResult = future.get(10, TimeUnit.SECONDS);
            System.out.println("<Success reply --------------->");
            final Response response = converter.fromMessage(consumedResult.value());
            System.out.println("Статус код ответа: " + response.getStatus().getCode());
            System.out.println("Сообщение: " + response.getMessage());
            System.out.println("Данные: " + response.getPayload());
        } catch (InterruptedException e) {
            System.out.println("Interrupted waiting response");
        } catch (ExecutionException e) {
            System.out.println("Failure execution response");
        } catch (TimeoutException e) {
            System.out.println("Failure by timeout waiting response");
        }
    }

}
