package com.bacheloros.bacheloros_backend.budget.repository;

import com.bacheloros.bacheloros_backend.budget.entity.MonthlyBudget;
import com.bacheloros.bacheloros_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {
    Optional<MonthlyBudget> findByUserAndMonthAndYear(User user, Integer month, Integer year);
}