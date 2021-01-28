package ru.dininslam.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dininslam.server.model.Bill;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String message;
    private Bill payload;
}
