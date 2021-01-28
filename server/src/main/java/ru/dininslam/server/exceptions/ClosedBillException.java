package ru.dininslam.server.exceptions;

public class ClosedBillException extends RuntimeException {
    public ClosedBillException(String message) {
        super(message);
    }
}
