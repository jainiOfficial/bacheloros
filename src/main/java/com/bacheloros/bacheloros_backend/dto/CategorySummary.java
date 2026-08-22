package com.bacheloros.bacheloros_backend.dto;

import java.math.BigDecimal;

public class CategorySummary {
    private String category;
    private BigDecimal totalSpent;
    private BigDecimal budget;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}