package ru.dininslam.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dininslam.client.dto.enums.Operation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    private Long sourceId;
    private Long targetId;
    private Operation operation;
    private Long value;
}
