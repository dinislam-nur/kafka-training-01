package ru.dininslam.server.kafka.excpetion_handler;

@FunctionalInterface
public interface Executor {

    void execute() throws Throwable;
}
