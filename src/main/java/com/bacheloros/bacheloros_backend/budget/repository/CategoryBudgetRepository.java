package com.bacheloros.bacheloros_backend.budget.repository;

import com.bacheloros.bacheloros_backend.budget.entity.CategoryBudget;
import com.bacheloros.bacheloros_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
    Optional<CategoryBudget> findByUserAndCategoryAndMonthAndYear(User user, String category, Integer month, Integer year);
    List<CategoryBudget> findByUserAndMonthAndYear(User user, Integer month, Integer year);
}