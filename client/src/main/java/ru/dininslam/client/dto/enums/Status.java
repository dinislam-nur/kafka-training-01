package ru.dininslam.client.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    SUCCESS(0),
    BILL_NOT_EXIST(15),
    BILL_IS_CLOSED(20),
    INSUFFICIENT_FOUNDS(25),
    WRONG_ARGUMENTS(30),
    INTERNAL_ERROR(35),
    DB_ERROR(40);

    private final Integer code;

}
