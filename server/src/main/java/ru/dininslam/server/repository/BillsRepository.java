package ru.dininslam.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.dininslam.server.model.Bill;

public interface BillsRepository extends CrudRepository<Bill, Long> {

}
