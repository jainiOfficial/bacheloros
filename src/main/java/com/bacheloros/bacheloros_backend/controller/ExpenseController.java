package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.CreateExpenseRequest;
import com.bacheloros.bacheloros_backend.dto.ExpenseResponse;
import com.bacheloros.bacheloros_backend.dto.UserResponse;
import com.bacheloros.bacheloros_backend.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody CreateExpenseRequest expenseRequest)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(expenseRequest));
    }
    @GetMapping
    public ResponseEntity<List<ExpenseResponse>>getExpenses()
    {
        return ResponseEntity.ok(expenseService.getMyExpenses());
    }
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable long id ,@RequestBody CreateExpenseRequest expenseRequest)
    {
         return ResponseEntity.ok(expenseService.updateExpense(id, expenseRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable long id)
    {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategoryAndMonth(
            @RequestParam String category,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return ResponseEntity.ok(expenseService.getExpensesByCategoryAndMonth(category, month, year));
    }
}
