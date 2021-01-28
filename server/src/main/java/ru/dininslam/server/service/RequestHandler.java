package ru.dininslam.server.service;

import ru.dininslam.server.dto.Request;
import ru.dininslam.server.dto.Response;

public interface RequestHandler {

    Response processRequest(Request request);
}
