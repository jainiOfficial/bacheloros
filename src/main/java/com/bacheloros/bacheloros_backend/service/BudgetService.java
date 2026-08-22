package com.bacheloros.bacheloros_backend.service;

import com.bacheloros.bacheloros_backend.dto.BudgetResponse;
import com.bacheloros.bacheloros_backend.dto.CreateBudgetRequest;
import com.bacheloros.bacheloros_backend.entity.Budget;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.BudgetRepository;
import com.bacheloros.bacheloros_backend.repository.ExpenseRepository;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetService {
  private final BudgetRepository budgetRepository;
  private final UserRepository userRepository;
  private final ExpenseRepository expenseRepository;
    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.expenseRepository=expenseRepository;
    }
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
    private BudgetResponse toResponse(Budget budget,User user)
    {
        BudgetResponse budgetResponse=new BudgetResponse();
        budgetResponse.setCategory(budget.getCategory());
        budgetResponse.setLimitAmount(budget.getLimitAmount());
        budgetResponse.setMonth(budget.getMonth());
        budgetResponse.setYear(budget.getYear());
        budgetResponse.setId(budget.getId());
        LocalDate startDate = LocalDate.of(budget.getYear(), budget.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        // calc spent and remaining amount
        BigDecimal spentAmount=expenseRepository.getTotalSpentByCategoryAndDateRange(user,budget.getCategory(),startDate,endDate);
        BigDecimal remainingAmount=budget.getLimitAmount().subtract(spentAmount);
        budgetResponse.setSpentAmount(spentAmount);
        budgetResponse.setRemainingAmount(remainingAmount);
        return budgetResponse;
    }
    public BudgetResponse createBudget(CreateBudgetRequest budgetRequest)
    {
        User user=getCurrentUser();
        Optional<Budget> existing = budgetRepository.findByUserAndCategoryAndMonthAndYear(
                user, budgetRequest.getCategory(), budgetRequest.getMonth(), budgetRequest.getYear());
        if (existing.isPresent()) {
            throw new RuntimeException("Budget already exists for this category and month");
        }
        Budget budget=new Budget();
        budget.setCategory(budgetRequest.getCategory());
        budget.setLimitAmount(budgetRequest.getLimitAmount());
        budget.setMonth(budgetRequest.getMonth());
        budget.setYear(budgetRequest.getYear());
        budget.setUser(user);
        budgetRepository.save(budget);
        return toResponse(budget,user);
    }
    public List<BudgetResponse>getAllBudgetsByMonthAndYear(Integer month,Integer year){
        User user=getCurrentUser();
        List<Budget> budgetList=budgetRepository.findByUserAndMonthAndYear(user,month,year);
        return budgetList.stream()
                .map(budget -> toResponse(budget,user))
                .collect(Collectors.toList());
    }
}
