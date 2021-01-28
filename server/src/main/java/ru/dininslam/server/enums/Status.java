package ru.dininslam.server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

    SUCCESS(0, "Успешная операция"),
    BILL_NOT_EXIST(15, "Счета не существует"),
    BILL_IS_CLOSED(20, "Счет закрыт"),
    INSUFFICIENT_FOUNDS(25, "Недостаточно средств"),
    WRONG_ARGUMENTS(30, "Неправильные аргументы"),
    INTERNAL_ERROR(35, "Внутренняя ошибка сервера"),
    DB_ERROR(40, "Ошибка базы данных");

    private final Integer code;
    private final String message;

}
