package com.bacheloros.bacheloros_backend.service;

import com.bacheloros.bacheloros_backend.dto.CreateExpenseRequest;
import com.bacheloros.bacheloros_backend.dto.ExpenseResponse;
import com.bacheloros.bacheloros_backend.entity.Expense;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.ExpenseRepository;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseService(UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }
    private ExpenseResponse toResponse(Expense expense)
    {
        ExpenseResponse expenseResponse=new ExpenseResponse();
        expenseResponse.setAmount(expense.getAmount());
        expenseResponse.setCategory(expense.getCategory());
        expenseResponse.setDate(expense.getDate());
        expenseResponse.setDescription(expense.getDescription());
        expenseResponse.setId(expense.getId());
        expenseResponse.setPaymentTo(expense.getPaymentTo());
        expenseResponse.setPaymentType(expense.getPaymentType());
        expenseResponse.setTitle(expense.getTitle());
        return expenseResponse;
    }
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public ExpenseResponse createExpense(CreateExpenseRequest expenseRequest)
    {
        User user=getCurrentUser();
        Expense expense=new Expense();
        expense.setTitle(expenseRequest.getTitle());
        expense.setAmount(expenseRequest.getAmount());
        expense.setCategory(expenseRequest.getCategory());
        expense.setDescription(expenseRequest.getDescription());
        expense.setDate(expenseRequest.getDate());
        expense.setPaymentType(expenseRequest.getPaymentType());
        expense.setPaymentTo(expenseRequest.getPaymentTo());
        expense.setUser(user);
        return toResponse(expenseRepository.save(expense));
    }
    public List<ExpenseResponse> getMyExpenses()
    {
        User user=getCurrentUser();
        List<Expense>expenseList=expenseRepository.findByUser(user);
        return expenseList.stream()
                .map(expense -> toResponse(expense))
                .collect(Collectors.toList());
    }

    public ExpenseResponse updateExpense(Long id,CreateExpenseRequest expenseRequest)
    {
        Expense expense=expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        User user=expense.getUser();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(email.equals(user.getEmail()))
        {
            expense.setTitle(expenseRequest.getTitle());
            expense.setAmount(expenseRequest.getAmount());
            expense.setCategory(expenseRequest.getCategory());
            expense.setDescription(expenseRequest.getDescription());
            expense.setDate(expenseRequest.getDate());
            expense.setPaymentType(expenseRequest.getPaymentType());
            expense.setPaymentTo(expenseRequest.getPaymentTo());
            return toResponse(expenseRepository.save(expense));

        }
        else {
            throw new RuntimeException("Un-authorize");
        }
    }

    public void deleteExpense(Long id)
    {
        Expense expense=expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        User user=expense.getUser();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(email.equals(user.getEmail()))
        {
            expenseRepository.delete(expense);

        }
        else {
            throw new RuntimeException("Un-authorize");
        }
    }
}
