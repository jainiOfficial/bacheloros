package com.bacheloros.bacheloros_backend.service;

import com.bacheloros.bacheloros_backend.budget.entity.MonthlyBudget;
import com.bacheloros.bacheloros_backend.budget.repository.MonthlyBudgetRepository;
import com.bacheloros.bacheloros_backend.dto.FinanceOverviewResponse;
import com.bacheloros.bacheloros_backend.entity.Bill;
import com.bacheloros.bacheloros_backend.entity.PeriodType;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.BillRepository;
import com.bacheloros.bacheloros_backend.repository.ExpenseRepository;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinanceService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final MonthlyBudgetRepository monthlyBudgetRepository;

    public FinanceService(ExpenseRepository expenseRepository, UserRepository userRepository, BillRepository billRepository,MonthlyBudgetRepository monthlyBudgetRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
        this.monthlyBudgetRepository=monthlyBudgetRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
    // "period" ko current date-range mein convert karta hai
    private LocalDate[] resolveCurrentRange(PeriodType period, LocalDate today) {
        switch (period) {
            case WEEK:
                LocalDate weekStart = today.with(DayOfWeek.MONDAY);
                return new LocalDate[]{weekStart, weekStart.plusDays(6)};
            case YEAR:
                return new LocalDate[]{today.withDayOfYear(1), today.withDayOfYear(today.lengthOfYear())};
            case MONTH:
            default:
                LocalDate monthStart = today.withDayOfMonth(1);
                return new LocalDate[]{monthStart, monthStart.withDayOfMonth(monthStart.lengthOfMonth())};
        }
    }

    // current range ko ek period peeche shift karta hai (% comparison ke liye)
    private LocalDate[] resolvePreviousRange(PeriodType period, LocalDate[] currentRange) {
        switch (period) {
            case WEEK:
                return new LocalDate[]{currentRange[0].minusWeeks(1), currentRange[1].minusWeeks(1)};
            case YEAR:
                return new LocalDate[]{currentRange[0].minusYears(1), currentRange[1].minusYears(1)};
            case MONTH:
            default:
                LocalDate prevMonthStart = currentRange[0].minusMonths(1);
                return new LocalDate[]{prevMonthStart, prevMonthStart.withDayOfMonth(prevMonthStart.lengthOfMonth())};
        }
    }

    public FinanceOverviewResponse getOverview() {
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();
        long billsPendingCount = billRepository.countByUserAndIsPaid(user, false);

        // 4. Overdue bills
        List<Bill> overdueBills = billRepository.findByUserAndIsPaidAndDueDateBefore(user, false, today);
        BigDecimal overdueAmount = overdueBills.stream()
                .map(Bill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. Budget remaining - hamesha CURRENT MONTH ka (period-filter se independent)
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
        BigDecimal monthExpense = expenseRepository.getTotalSpentByDateRange(user, monthStart, monthEnd);

        BigDecimal totalBudgetAmount = monthlyBudgetRepository
                .findByUserAndMonthAndYear(user, today.getMonthValue(), today.getYear())
                .map(MonthlyBudget::getTotalAmount)
                .orElse(BigDecimal.ZERO);
        BigDecimal budgetRemaining = totalBudgetAmount.subtract(monthExpense);

        FinanceOverviewResponse response = new FinanceOverviewResponse();
        response.setBillsPendingCount(billsPendingCount);
        response.setOverdueBillsAmount(overdueAmount);
        response.setOverdueBillsCount(overdueBills.size());
        response.setTotalBudgetAmount(totalBudgetAmount);
        response.setBudgetRemaining(budgetRemaining);
        response.setMonthExpense(monthExpense);
        return response;
    }
}
