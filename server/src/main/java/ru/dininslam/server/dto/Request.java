package ru.dininslam.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.dininslam.server.enums.Operation;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotNull
    private Long sourceId;
    private Long targetId;
    @NotNull
    private Operation operation;
    private Long value;
}
