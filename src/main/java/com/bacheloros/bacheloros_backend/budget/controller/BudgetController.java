package com.bacheloros.bacheloros_backend.budget.controller;

import com.bacheloros.bacheloros_backend.budget.dto.BudgetOverviewResponse;
import com.bacheloros.bacheloros_backend.budget.dto.SaveBudgetRequest;
import com.bacheloros.bacheloros_backend.budget.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<BudgetOverviewResponse> saveBudget(@RequestBody SaveBudgetRequest request) {
        return ResponseEntity.ok(budgetService.saveBudget(request));
    }

    @GetMapping
    public ResponseEntity<BudgetOverviewResponse> getOverview(@RequestParam Integer month, @RequestParam Integer year) {
        return ResponseEntity.ok(budgetService.getOverview(month, year));
    }
}