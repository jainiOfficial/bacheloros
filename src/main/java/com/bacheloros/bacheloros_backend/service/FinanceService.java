package com.bacheloros.bacheloros_backend.service;

import com.bacheloros.bacheloros_backend.dto.FinanceOverviewResponse;
import com.bacheloros.bacheloros_backend.entity.Bill;
import com.bacheloros.bacheloros_backend.entity.Budget;
import com.bacheloros.bacheloros_backend.entity.PeriodType;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.BillRepository;
import com.bacheloros.bacheloros_backend.repository.BudgetRepository;
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
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;

    public FinanceService(ExpenseRepository expenseRepository, BudgetRepository budgetRepository, UserRepository userRepository, BillRepository billRepository) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
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

    public FinanceOverviewResponse getOverview(PeriodType period) {
        User user = getCurrentUser();
        LocalDate today = LocalDate.now();

        LocalDate[] currentRange = resolveCurrentRange(period, today);
        LocalDate[] previousRange = resolvePreviousRange(period, currentRange);

        // 1. Period expense + % change
        BigDecimal periodExpense = expenseRepository.getTotalSpentByDateRange(user, currentRange[0], currentRange[1]);
        BigDecimal previousExpense = expenseRepository.getTotalSpentByDateRange(user, previousRange[0], previousRange[1]);

        Double percentChange = null;
        if (previousExpense.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal diff = periodExpense.subtract(previousExpense);
            percentChange = diff.divide(previousExpense, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        // 2. Budget remaining - hamesha CURRENT MONTH ka (period-filter se independent)
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
        List<Budget> monthBudgets = budgetRepository.findByUserAndMonthAndYear(user, today.getMonthValue(), today.getYear());
        BigDecimal totalBudgeted = monthBudgets.stream()
                .map(Budget::getLimitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal monthSpent = expenseRepository.getTotalSpentByDateRange(user, monthStart, monthEnd);
        BigDecimal budgetRemaining = totalBudgeted.subtract(monthSpent);

        // 3. Bills pending (period-independent — total unpaid count)
        long billsPendingCount = billRepository.countByUserAndIsPaid(user, false);

        // 4. Overdue bills
        List<Bill> overdueBills = billRepository.findByUserAndIsPaidAndDueDateBefore(user, false, today);
        BigDecimal overdueAmount = overdueBills.stream()
                .map(Bill::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        FinanceOverviewResponse response = new FinanceOverviewResponse();
        response.setBillsPendingCount(billsPendingCount);
        response.setBudgetRemaining(budgetRemaining);
        response.setPeriodExpense(periodExpense);
        response.setPercentChangeVsLastPeriod(percentChange);
        response.setOverdueBillsAmount(overdueAmount);
        response.setOverdueBillsCount(overdueBills.size());
        return response;
    }
}
