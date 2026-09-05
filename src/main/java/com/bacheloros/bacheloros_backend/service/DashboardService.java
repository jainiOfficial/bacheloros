package com.bacheloros.bacheloros_backend.service;

import com.bacheloros.bacheloros_backend.dto.BillResponse;
import com.bacheloros.bacheloros_backend.dto.DashboardResponse;
import com.bacheloros.bacheloros_backend.entity.Bill;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.BillRepository;
import com.bacheloros.bacheloros_backend.repository.ExpenseRepository;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;

    public DashboardService(ExpenseRepository expenseRepository, UserRepository userRepository, BillRepository billRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    private BillResponse toBillResponse(Bill bill) {
        BillResponse response = new BillResponse();
        response.setId(bill.getId());
        response.setTitle(bill.getTitle());
        response.setDescription(bill.getDescription());
        response.setAmount(bill.getAmount());
        response.setPaidTo(bill.getPaidTo());
        response.setDueDate(bill.getDueDate());
        response.setPaid(bill.isPaid());
        response.setRecurring(bill.isRecurring());
        response.setRecurrenceType(bill.getRecurrenceType());
        return response;
    }

    public DashboardResponse getDashboard(Integer month, Integer year) {
        User user = getCurrentUser();

        LocalDate startDate = LocalDate.of(year, month, 1);//month,year leke month ka pehla din nikal liya
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());//last date nikal li month ki

        BigDecimal totalSpent = expenseRepository.getTotalSpentByDateRange(user, startDate, endDate);

        //Dashboard pe show ho ki konse Unpaid bills h month me.
        List<Bill> unpaidBills = billRepository.findByUserAndDueDateBetweenAndIsPaid(
                user, startDate, endDate, false);

        List<BillResponse> upcomingBills = unpaidBills.stream()
                .map(this::toBillResponse)
                .collect(Collectors.toList());

        DashboardResponse response = new DashboardResponse();
        response.setTotalSpent(totalSpent);
        response.setTotalBudgeted(BigDecimal.valueOf(0));
        response.setRemaining(BigDecimal.valueOf(0));
        response.setUpcomingBills(upcomingBills);
        //returning response
        return response;
    }
}
