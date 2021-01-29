package ru.dininslam.client.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dininslam.client.dto.Request;
import ru.dininslam.client.dto.enums.Operation;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class PerformerImpl implements Performer {

    private final Scanner scanner;
    private final KafkaBillService service;

    @Override
    public void perform() {
        final Request request = new Request();
        inputOperation(request);
        service.sendAndReceive(request);
    }

    private void inputOperation(Request request) {
        System.out.println(
                "Выберите номер операции:" + System.lineSeparator() +
                        "1 - Создать," + System.lineSeparator() +
                        "2 - Пополнить," + System.lineSeparator() +
                        "3 - Снять," + System.lineSeparator() +
                        "4 - Перевести," + System.lineSeparator() +
                        "5 - Закрыть"
        );
        try {
            setOperation(request, scanner.nextInt());
            inputSourceId(request);
        } catch (IllegalArgumentException ex) {
            System.out.println("Неправильное значение! Повторите операцию.");
            inputOperation(request);
        } catch (InputMismatchException ex) {
            System.out.println("Некорректное значение!");
            scanner.next();
            inputOperation(request);
        }
    }

    private void setOperation(Request request, int number) {
        switch (number) {
            case 1: {
                request.setOperation(Operation.CREATE);
                break;
            }
            case 2: {
                request.setOperation(Operation.FILL);
                break;
            }
            case 3: {
                request.setOperation(Operation.WRITE_OFF);
                break;
            }
            case 4: {
                request.setOperation(Operation.TRANSFER);
                break;
            }
            case 5: {
                request.setOperation(Operation.CLOSE);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    private void inputSourceId(Request request) {
        try {
            System.out.println("Введите (Long) номер счет:");
            request.setSourceId(scanner.nextLong());
            final Operation operation = request.getOperation();
            if (Operation.TRANSFER.equals(operation)) {
                inputTargetId(request);
            } else if (!Operation.CREATE.equals(operation)) {
                inputValue(request);
            }
        } catch (InputMismatchException ex) {
            System.out.println("Некорректное значение!");
            scanner.next();
            inputSourceId(request);
        }
    }

    private void inputTargetId(Request request) {
        try {
            System.out.println("Введите (Long) номер счета, на который собираетесь перевести средства:");
            request.setTargetId(scanner.nextLong());
            inputValue(request);
        } catch (InputMismatchException ex) {
            System.out.println("Некорректное значение!");
            scanner.next();
            inputTargetId(request);
        }
    }

    private void inputValue(Request request) {
        try {
            System.out.println("Введите (Long) значение для совершения операции:");
            request.setValue(scanner.nextLong());
        } catch (InputMismatchException ex) {
            System.out.println("Некорректное значение!");
            scanner.next();
            inputValue(request);
        }
    }
}
