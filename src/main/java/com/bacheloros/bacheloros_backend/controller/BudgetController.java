package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.BudgetResponse;
import com.bacheloros.bacheloros_backend.dto.CreateBudgetRequest;
import com.bacheloros.bacheloros_backend.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;

    }
    @PostMapping
    public ResponseEntity<BudgetResponse>createBudget(@RequestBody CreateBudgetRequest createBudgetRequest)
    {
        return ResponseEntity.ok(budgetService.createBudget(createBudgetRequest));
    }
    @GetMapping
    public ResponseEntity<List<BudgetResponse>>getAllBudgetsByMonthAndYear(@RequestParam Integer month,@RequestParam Integer year){
        return ResponseEntity.ok(budgetService.getAllBudgetsByMonthAndYear(month,year));
    }
}
