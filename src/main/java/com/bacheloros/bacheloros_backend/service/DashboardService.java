package com.bacheloros.bacheloros_backend.service;

import com.bacheloros.bacheloros_backend.dto.BillResponse;
import com.bacheloros.bacheloros_backend.dto.CategorySummary;
import com.bacheloros.bacheloros_backend.dto.DashboardResponse;
import com.bacheloros.bacheloros_backend.entity.Bill;
import com.bacheloros.bacheloros_backend.entity.Budget;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.BillRepository;
import com.bacheloros.bacheloros_backend.repository.BudgetRepository;
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
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;

    public DashboardService(ExpenseRepository expenseRepository, BudgetRepository budgetRepository, UserRepository userRepository, BillRepository billRepository) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
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

        List<Budget> budgets = budgetRepository.findByUserAndMonthAndYear(user, month, year);
        BigDecimal totalBudgeted = budgets.stream()
                .map(Budget::getLimitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remaining = totalBudgeted.subtract(totalSpent);
        List<CategorySummary> categoryBreakdown = budgets.stream()
                .map(budget -> {
                    BigDecimal categorySpent = expenseRepository.getTotalSpentByCategoryAndDateRange(
                            user, budget.getCategory(), startDate, endDate);

                    CategorySummary summary = new CategorySummary();
                    summary.setCategory(budget.getCategory());
                    summary.setBudget(budget.getLimitAmount());
                    summary.setTotalSpent(categorySpent);
                    return summary;
                })
                .collect(Collectors.toList());

        List<Bill> unpaidBills = billRepository.findByUserAndDueDateBetweenAndIsPaid(
                user, startDate, endDate, false);

        List<BillResponse> upcomingBills = unpaidBills.stream()
                .map(this::toBillResponse)
                .collect(Collectors.toList());

        DashboardResponse response = new DashboardResponse();
        response.setTotalSpent(totalSpent);
        response.setTotalBudgeted(totalBudgeted);
        response.setRemaining(remaining);
        response.setCategoryBreakdown(categoryBreakdown);
        response.setUpcomingBills(upcomingBills);
        //returning response
        return response;
    }
}
