package ru.dininslam.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dininslam.server.dto.Request;
import ru.dininslam.server.dto.Response;
import ru.dininslam.server.enums.Operation;
import ru.dininslam.server.service.BillService;
import ru.dininslam.server.service.RequestHandler;

import javax.validation.ValidationException;


@Service
@RequiredArgsConstructor
public class RequestHandlerImpl implements RequestHandler {

    private final BillService billService;

    @Override
    public Response processRequest(Request request) {
        validate(request);
        final Operation operation = request.getOperation();
        switch (operation) {
            case CREATE: {
                return billService.create(request.getSourceId());
            }
            case FILL: {
                return billService.fill(request.getSourceId(), request.getValue());
            }
            case CLOSE:{
                return billService.close(request.getSourceId());
            }
            case TRANSFER: {
                return billService.transfer(request.getSourceId(), request.getTargetId(), request.getValue());
            }
            case WRITE_OFF: {
                return billService.writeOff(request.getSourceId(), request.getValue());
            }
            default: {
                throw new IllegalStateException();
            }
        }
    }

    private void validate(Request request) {
        if(request.getSourceId() == null || request.getOperation() == null) {
            throw new ValidationException();
        }
    }
}
