package com.bacheloros.bacheloros_backend.dto;

import com.bacheloros.bacheloros_backend.entity.User;

import java.math.BigDecimal;

public class CreateBudgetRequest {
    private String category;
    private BigDecimal limitAmount;
    private Integer month;
    private Integer year;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

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

}

