package com.bacheloros.bacheloros_backend.repository;

import com.bacheloros.bacheloros_backend.entity.Bill;
import com.bacheloros.bacheloros_backend.entity.RecurrenceType;
import com.bacheloros.bacheloros_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill,Long> {
    List<Bill> findBillsByUser(User user);
    List<Bill> findBillsByUserAndIsPaid(User user, boolean isPaid);//find all unpaid bills
    List<Bill> findByUserAndRecurrenceTypeAndIsPaid(User user, RecurrenceType recurrenceType, boolean isPaid);// get all paid/unpaid bills according to recurrenceType
    List<Bill> findByUserAndDueDateBetweenAndIsPaid(User user, LocalDate startDate, LocalDate endDate, boolean isPaid);//is user ke, is date range mein due, aur unpaid bills dikhao" — jaise Dashboard pe "upcoming bills"
}
