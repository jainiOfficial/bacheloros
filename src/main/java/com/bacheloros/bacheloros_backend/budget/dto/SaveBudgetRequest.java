package com.bacheloros.bacheloros_backend.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public class SaveBudgetRequest {

    private Integer month;
    private Integer year;
    private BigDecimal totalAmount;
    private List<CategoryAllocationRequest> categories;

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

    public List<CategoryAllocationRequest> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryAllocationRequest> categories) {
        this.categories = categories;
    }
}