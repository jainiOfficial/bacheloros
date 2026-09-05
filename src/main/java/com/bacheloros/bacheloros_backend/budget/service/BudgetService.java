package com.bacheloros.bacheloros_backend.budget.service;

import com.bacheloros.bacheloros_backend.budget.dto.*;
import com.bacheloros.bacheloros_backend.budget.entity.CategoryBudget;
import com.bacheloros.bacheloros_backend.budget.entity.MonthlyBudget;
import com.bacheloros.bacheloros_backend.budget.repository.CategoryBudgetRepository;
import com.bacheloros.bacheloros_backend.budget.repository.MonthlyBudgetRepository;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.exception.InvalidBudgetMonthException;
import com.bacheloros.bacheloros_backend.repository.ExpenseRepository;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(MonthlyBudgetRepository monthlyBudgetRepository,
                         CategoryBudgetRepository categoryBudgetRepository,
                         UserRepository userRepository,
                         ExpenseRepository expenseRepository) {
        this.monthlyBudgetRepository = monthlyBudgetRepository;
        this.categoryBudgetRepository = categoryBudgetRepository;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ---------- SAVE (upsert Total + Categories in one call) ----------

    public BudgetOverviewResponse saveBudget(SaveBudgetRequest request) {
        User user = getCurrentUser();

        // 1. Validate: only current month allowed
        LocalDate now = LocalDate.now();
        if (!request.getMonth().equals(now.getMonthValue()) || !request.getYear().equals(now.getYear())) {
            throw new InvalidBudgetMonthException("Budget can only be set for the current month");
        }

        // 2. Upsert MonthlyBudget
        MonthlyBudget monthlyBudget = monthlyBudgetRepository
                .findByUserAndMonthAndYear(user, request.getMonth(), request.getYear())
                .orElse(new MonthlyBudget());

        monthlyBudget.setUser(user);
        monthlyBudget.setMonth(request.getMonth());
        monthlyBudget.setYear(request.getYear());
        monthlyBudget.setTotalAmount(request.getTotalAmount());
        monthlyBudgetRepository.save(monthlyBudget);

        // 3. Upsert each CategoryBudget (if any provided)
        if (request.getCategories() != null) {
            for (CategoryAllocationRequest catReq : request.getCategories()) {
                CategoryBudget categoryBudget = categoryBudgetRepository
                        .findByUserAndCategoryAndMonthAndYear(user, catReq.getCategory(), request.getMonth(), request.getYear())
                        .orElse(new CategoryBudget());

                categoryBudget.setUser(user);
                categoryBudget.setCategory(catReq.getCategory());
                categoryBudget.setMonth(request.getMonth());
                categoryBudget.setYear(request.getYear());
                categoryBudget.setAllocatedAmount(catReq.getAllocatedAmount());
                categoryBudgetRepository.save(categoryBudget);
            }
        }

        // 4. Return the full overview (reuse getOverview logic)
        return getOverview(request.getMonth(), request.getYear());
    }

    // ---------- GET (view overview for any month, including past) ----------

    public BudgetOverviewResponse getOverview(Integer month, Integer year) {
        User user = getCurrentUser();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Optional<MonthlyBudget> monthlyBudgetOpt = monthlyBudgetRepository.findByUserAndMonthAndYear(user, month, year);
        BigDecimal totalAmount = monthlyBudgetOpt.map(MonthlyBudget::getTotalAmount).orElse(BigDecimal.ZERO);

        BigDecimal totalSpent = expenseRepository.getTotalSpentByDateRange(user, startDate, endDate);
        BigDecimal totalRemaining = totalAmount.subtract(totalSpent);

        List<CategoryBudget> categoryBudgets = categoryBudgetRepository.findByUserAndMonthAndYear(user, month, year);
        List<CategoryBudgetResponse> categoryResponses = new ArrayList<>();

        for (CategoryBudget cb : categoryBudgets) {
            BigDecimal spent = expenseRepository.getTotalSpentByCategoryAndDateRange(user, cb.getCategory(), startDate, endDate);
            CategoryBudgetResponse catResp = new CategoryBudgetResponse();
            catResp.setCategory(cb.getCategory());
            catResp.setAllocatedAmount(cb.getAllocatedAmount());
            catResp.setSpentAmount(spent);
            catResp.setRemainingAmount(cb.getAllocatedAmount().subtract(spent));
            categoryResponses.add(catResp);
        }

        BudgetOverviewResponse response = new BudgetOverviewResponse();
        response.setMonth(month);
        response.setYear(year);
        response.setTotalAmount(totalAmount);
        response.setTotalSpent(totalSpent);
        response.setTotalRemaining(totalRemaining);
        response.setCategories(categoryResponses);
        return response;
    }
}