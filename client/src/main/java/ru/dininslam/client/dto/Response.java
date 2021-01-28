package ru.dininslam.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dininslam.client.dto.enums.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Status status;
    private String message;
    private Bill payload;
}
