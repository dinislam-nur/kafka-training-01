package ru.dininslam.server.kafka.excpetion_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import ru.dininslam.server.dto.Response;
import ru.dininslam.server.enums.Status;
import ru.dininslam.server.exceptions.ClosedBillException;
import ru.dininslam.server.exceptions.InsufficientFundsException;
import ru.dininslam.server.exceptions.ObjectNotFoundException;
import ru.dininslam.server.kafka.Producer;

import javax.validation.ValidationException;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerImpl implements ExceptionHandler {

    private final Producer producer;

    @Override
    public void handle(Executor task, byte[] correlationId) {
        try {
            task.execute();
        } catch (ObjectNotFoundException ex) {
            handleObjectNotFound(correlationId);
        } catch (ClosedBillException ex) {
            handleClosedBill(correlationId, ex);
        } catch (InsufficientFundsException ex) {
            handleInsufficientFunds(correlationId);
        } catch (ValidationException ex) {
            handleValidation(correlationId);
        } catch (DataAccessException ex) {
            handleDB(correlationId);
        } catch (Throwable throwable) {
            handleThrowable(correlationId);
        }
    }

    private void handleObjectNotFound(byte[] correlationId) {
        producer.send(Response.builder()
                        .status(Status.BILL_NOT_EXIST)
                        .message(Status.BILL_NOT_EXIST.getMessage())
                        .build(),
                correlationId);
    }

    private void handleClosedBill(byte[] correlationId, ClosedBillException closed) {
        producer.send(Response.builder()
                        .status(Status.BILL_IS_CLOSED)
                        .message(closed.getMessage())
                        .build(),
                correlationId);
    }

    private void handleInsufficientFunds(byte[] correlationId) {
        producer.send(Response.builder()
                        .status(Status.INSUFFICIENT_FOUNDS)
                        .message(Status.INSUFFICIENT_FOUNDS.getMessage())
                        .build(),
                correlationId);
    }

    private void handleDB(byte[] correlationId) {
        producer.send(Response.builder()
                        .status(Status.DB_ERROR)
                        .message(Status.DB_ERROR.getMessage())
                        .build(),
                correlationId);
    }

    private void handleThrowable(byte[] correlationId) {
        producer.send(Response.builder()
                        .status(Status.INTERNAL_ERROR)
                        .message(Status.INTERNAL_ERROR.getMessage())
                        .build(),
                correlationId);
    }

    private void handleValidation(byte[] correlationId) {
        producer.send(Response.builder()
                        .status(Status.WRONG_ARGUMENTS)
                        .message(Status.WRONG_ARGUMENTS.getMessage())
                        .build(),
                correlationId);
    }
}
