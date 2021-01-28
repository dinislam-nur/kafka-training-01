package ru.dininslam.server.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ServerMessageConverter {

    private final ObjectMapper objectMapper;

    public String toMessage(Response response) {
        String message = null;
        try {
            message = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public Request fromMessage(String message) {
        Request request = null;
        try {
            request = objectMapper.readValue(message, Request.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return request;
    }
}
