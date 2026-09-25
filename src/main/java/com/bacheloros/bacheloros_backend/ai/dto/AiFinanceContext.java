package com.bacheloros.bacheloros_backend.ai.dto;

import com.bacheloros.bacheloros_backend.budget.dto.BudgetOverviewResponse;
import com.bacheloros.bacheloros_backend.dto.BillResponse;
import com.bacheloros.bacheloros_backend.dto.ExpenseResponse;
import com.bacheloros.bacheloros_backend.dto.FinanceOverviewResponse;

import java.util.List;

public class AiFinanceContext {
    private FinanceOverviewResponse currentMonthSummary;
    private BudgetOverviewResponse currentMonthBudget;
    private BudgetOverviewResponse previousMonthBudget;
    private List<ExpenseResponse> currentMonthExpenses;
    private List<BillResponse> allBills;

    // getters + setters (sab)


    public FinanceOverviewResponse getCurrentMonthSummary() {
        return currentMonthSummary;
    }

    public void setCurrentMonthSummary(FinanceOverviewResponse currentMonthSummary) {
        this.currentMonthSummary = currentMonthSummary;
    }

    public BudgetOverviewResponse getCurrentMonthBudget() {
        return currentMonthBudget;
    }

    public void setCurrentMonthBudget(BudgetOverviewResponse currentMonthBudget) {
        this.currentMonthBudget = currentMonthBudget;
    }

    public BudgetOverviewResponse getPreviousMonthBudget() {
        return previousMonthBudget;
    }

    public void setPreviousMonthBudget(BudgetOverviewResponse previousMonthBudget) {
        this.previousMonthBudget = previousMonthBudget;
    }

    public List<ExpenseResponse> getCurrentMonthExpenses() {
        return currentMonthExpenses;
    }

    public void setCurrentMonthExpenses(List<ExpenseResponse> currentMonthExpenses) {
        this.currentMonthExpenses = currentMonthExpenses;
    }

    public List<BillResponse> getAllBills() {
        return allBills;
    }

    public void setAllBills(List<BillResponse> allBills) {
        this.allBills = allBills;
    }
}
