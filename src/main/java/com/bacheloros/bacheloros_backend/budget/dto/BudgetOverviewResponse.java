package com.bacheloros.bacheloros_backend.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public class BudgetOverviewResponse {

    private Integer month;
    private Integer year;
    private BigDecimal totalAmount;
    private BigDecimal totalSpent;
    private BigDecimal totalRemaining;
    private List<CategoryBudgetResponse> categories;

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getTotalRemaining() {
        return totalRemaining;
    }

    public void setTotalRemaining(BigDecimal totalRemaining) {
        this.totalRemaining = totalRemaining;
    }

    public List<CategoryBudgetResponse> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryBudgetResponse> categories) {
        this.categories = categories;
    }
}