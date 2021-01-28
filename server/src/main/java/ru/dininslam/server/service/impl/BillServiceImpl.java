package ru.dininslam.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dininslam.server.dto.Response;
import ru.dininslam.server.exceptions.ClosedBillException;
import ru.dininslam.server.exceptions.InsufficientFundsException;
import ru.dininslam.server.exceptions.ObjectNotFoundException;
import ru.dininslam.server.model.Bill;
import ru.dininslam.server.repository.BillsRepository;
import ru.dininslam.server.service.BillService;

import javax.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillsRepository billsRepository;

    @Override
    @Transactional
    public Response fill(Long billId, @NotNull Long value) {
        final Bill bill = billsRepository.findById(billId).orElseThrow(ObjectNotFoundException::new);
        checkClosure(bill);
        bill.setValue(bill.getValue() + value);
        final Bill save = billsRepository.save(bill);
        return Response.builder()
                .message("Успешное пополнение денежных средств")
                .payload(save)
                .build();
    }

    @Override
    @Transactional
    public Response writeOff(Long billId, @NotNull Long value) {
        final Bill bill = billsRepository.findById(billId).orElseThrow(ObjectNotFoundException::new);
        checkClosure(bill);
        final long remainder = bill.getValue() - value;
        checkReminder(remainder);
        bill.setValue(remainder);
        final Bill save = billsRepository.save(bill);
        return Response.builder()
                .message("Успешное снятие денежных средств")
                .payload(save)
                .build();
    }

    @Override
    @Transactional
    public Response transfer(Long sourceId, @NotNull Long targetId, @NotNull Long value) {
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
                .message("Успешное закрытие счета")
                .payload(save)
                .build();
    }

    private void checkClosure(Bill bill) {
        if (bill.getClosed()) {
            throw new ClosedBillException();
        }
    }

    private void checkReminder(Long reminder) {
        if (reminder < 0) {
            throw new InsufficientFundsException();
        }
    }
}
