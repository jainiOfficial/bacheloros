package com.bacheloros.bacheloros_backend.dto;

import java.math.BigDecimal;

public class FinanceOverviewResponse {
    private long billsPendingCount;
    private BigDecimal budgetRemaining;
    private BigDecimal periodExpense;
    private Double percentChangeVsLastPeriod; // null jab pichle period mein 0 expense tha (divide-by-zero avoid)
    private BigDecimal overdueBillsAmount;
    private long overdueBillsCount;

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

    public BigDecimal getPeriodExpense() {
        return periodExpense;
    }

    public void setPeriodExpense(BigDecimal periodExpense) {
        this.periodExpense = periodExpense;
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
}
