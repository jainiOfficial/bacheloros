package com.bacheloros.bacheloros_backend.exception;

public class InvalidBudgetMonthException extends RuntimeException {
    public InvalidBudgetMonthException(String message) {
        super(message);
    }
}