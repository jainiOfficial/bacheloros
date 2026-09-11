package com.bacheloros.bacheloros_backend.dto;

import java.math.BigDecimal;

public class FinanceOverviewResponse {
    private long billsPendingCount;
    private BigDecimal budgetRemaining;
    private BigDecimal monthExpense;
    private Double percentChangeVsLastPeriod; // null jab pichle period mein 0 expense tha (divide-by-zero avoid)
    private BigDecimal overdueBillsAmount;
    private long overdueBillsCount;
    private BigDecimal totalBudgetAmount;

    public long getBillsPendingCount() {
        return billsPendingCount;
    }

    public void setBillsPendingCount(long billsPendingCount) {
        this.billsPendingCount = billsPendingCount;
    }

    public BigDecimal getBudgetRemaining() {
        return budgetRemaining;
    }

    public void setBudgetRemaining(BigDecimal budgetRemaining) {
        this.budgetRemaining = budgetRemaining;
    }

    public Double getPercentChangeVsLastPeriod() {
        return percentChangeVsLastPeriod;
    }

    public void setPercentChangeVsLastPeriod(Double percentChangeVsLastPeriod) {
        this.percentChangeVsLastPeriod = percentChangeVsLastPeriod;
    }

    public BigDecimal getMonthExpense() {
        return monthExpense;
    }

    public void setMonthExpense(BigDecimal periodExpense) {
        this.monthExpense = periodExpense;
    }

    public BigDecimal getOverdueBillsAmount() {
        return overdueBillsAmount;
    }

    public void setOverdueBillsAmount(BigDecimal overdueBillsAmount) {
        this.overdueBillsAmount = overdueBillsAmount;
    }

    public long getOverdueBillsCount() {
        return overdueBillsCount;
    }

    public void setOverdueBillsCount(long overdueBillsCount) {
        this.overdueBillsCount = overdueBillsCount;
    }

    public BigDecimal getTotalBudgetAmount() {
        return totalBudgetAmount;
    }

    public void setTotalBudgetAmount(BigDecimal totalBudgetAmount) {
        this.totalBudgetAmount = totalBudgetAmount;
    }
}
