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
    long countByUserAndIsPaid(User user, boolean isPaid); // "Bill Pending" count ke liye — efficient, poori list nahi laata, sirf count
    List<Bill> findByUserAndIsPaidAndDueDateBefore(User user, boolean isPaid, LocalDate date);

    List<Bill> findByUserAndIsPaidAndDueDateGreaterThanEqualOrderByDueDateAsc(
            User user, boolean isPaid, LocalDate date);

    // Overdue list ko bhi ab sorted chahiye (pehle sirf overdue-amount ke liye use ho raha tha, ab list dikhani hai)
    List<Bill> findByUserAndIsPaidAndDueDateBeforeOrderByDueDateAsc(
            User user, boolean isPaid, LocalDate date);

    // Paid list - sabse recent due-date upar
    List<Bill> findByUserAndIsPaidOrderByDueDateDesc(User user, boolean isPaid);

    // Badge-counts ke liye lightweight count-queries (poori list nahi laate)
    long countByUserAndIsPaidAndDueDateGreaterThanEqual(User user, boolean isPaid, LocalDate date);
    long countByUserAndIsPaidAndDueDateBefore(User user, boolean isPaid, LocalDate date);
}
