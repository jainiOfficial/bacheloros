package com.bacheloros.bacheloros_backend.budget.dto;

import java.math.BigDecimal;

public class CategoryAllocationRequest {

    private String category;
    private BigDecimal allocatedAmount;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }
}