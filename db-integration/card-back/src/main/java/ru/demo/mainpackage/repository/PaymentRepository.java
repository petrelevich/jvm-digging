package ru.demo.mainpackage.repository;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.demo.mainpackage.model.Payment;

public interface PaymentRepository extends ListCrudRepository<Payment, Long> {

    @Query("select * from payments where status = 'NEW' order by id for update skip locked limit 10 ")
    List<Payment> findNewPayments();
}
