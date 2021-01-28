package ru.dininslam.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientMessageConverter {

    private final ObjectMapper objectMapper;

    public String toMessage(Request request){
        String message = null;
            try {
                message = objectMapper.writeValueAsString(request);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        return message;
    }

    public Response fromMessage(String message) {
        Response response = null;
        try {
            response = objectMapper.readValue(message, Response.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }
}
