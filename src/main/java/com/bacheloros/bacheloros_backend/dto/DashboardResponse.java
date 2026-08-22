package com.bacheloros.bacheloros_backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponse {
    private BigDecimal totalSpent;
    private BigDecimal totalBudgeted;
    private BigDecimal remaining;
    private List<CategorySummary> categoryBreakdown;
    private List<BillResponse> upcomingBills;

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getTotalBudgeted() {
        return totalBudgeted;
    }

    public void setTotalBudgeted(BigDecimal totalBudgeted) {
        this.totalBudgeted = totalBudgeted;
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
    }

    public List<CategorySummary> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategorySummary> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public List<BillResponse> getUpcomingBills() {
        return upcomingBills;
    }

    public void setUpcomingBills(List<BillResponse> upcomingBills) {
        this.upcomingBills = upcomingBills;
    }
}
