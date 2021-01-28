package ru.dininslam.server.kafka.excpetion_handler;

public interface ExceptionHandler {

    void handle(Executor task, byte[] correlationId);

}
