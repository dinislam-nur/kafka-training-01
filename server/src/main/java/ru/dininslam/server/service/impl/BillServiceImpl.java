package ru.dininslam.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dininslam.server.dto.Response;
import ru.dininslam.server.enums.Status;
import ru.dininslam.server.exceptions.ClosedBillException;
import ru.dininslam.server.exceptions.InsufficientFundsException;
import ru.dininslam.server.exceptions.ObjectNotFoundException;
import ru.dininslam.server.model.Bill;
import ru.dininslam.server.repository.BillsRepository;
import ru.dininslam.server.service.BillService;

import javax.validation.ValidationException;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillsRepository billsRepository;

    @Override
    @Transactional
    public Response fill(Long billId, Long value) {
        validateArguments(value);
        final Bill bill = billsRepository.findById(billId).orElseThrow(ObjectNotFoundException::new);
        checkClosure(bill);
        bill.setValue(bill.getValue() + value);
        final Bill save = billsRepository.save(bill);
        return Response.builder()
                .status(Status.SUCCESS)
                .message("Успешное пополнение денежных средств")
                .payload(save)
                .build();
    }

    @Override
    @Transactional
    public Response writeOff(Long billId, Long value) {
        validateArguments(value);
        final Bill bill = billsRepository.findById(billId).orElseThrow(ObjectNotFoundException::new);
        checkClosure(bill);
        final long remainder = bill.getValue() - value;
        checkReminder(remainder);
        bill.setValue(remainder);
        final Bill save = billsRepository.save(bill);
        return Response.builder()
                .status(Status.SUCCESS)
                .message("Успешное снятие денежных средств")
                .payload(save)
                .build();
    }

    @Override
    @Transactional
    public Response transfer(Long sourceId, Long targetId, Long value) {
        validateArguments(targetId, value);
        final Response wroteOff = writeOff(sourceId, value);
        fill(targetId, value);
        wroteOff.setMessage("Успешный перевод денежных средств");
        return wroteOff;
    }

    @Override
    @Transactional
    public Response create(Long billId) {
        final Bill save = billsRepository.save(Bill.builder()
                .id(billId)
                .build());
        return Response.builder()
                .status(Status.SUCCESS)
                .message("Успешное создание счета")
                .payload(save)
                .build();
    }

    @Override
    @Transactional
    public Response close(Long billId) {
        final Bill bill = billsRepository.findById(billId).orElseThrow(ObjectNotFoundException::new);
        bill.setClosed(true);
        final Bill save = billsRepository.save(bill);
        return Response.builder()
                .status(Status.SUCCESS)
                .message("Успешное закрытие счета")
                .payload(save)
                .build();
    }

    private void checkClosure(Bill bill) {
        if (bill.getClosed()) {
            throw new ClosedBillException("Счет номер " + bill.getId() + " закрыт!");
        }
    }

    private void checkReminder(Long reminder) {
        if (reminder < 0) {
            throw new InsufficientFundsException();
        }
    }

    private void validateArguments(Object...args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new ValidationException();
            }
        }
    }
}
