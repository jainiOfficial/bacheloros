package com.bacheloros.bacheloros_backend.repository;

import com.bacheloros.bacheloros_backend.entity.Budget;
import com.bacheloros.bacheloros_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUser(User user);

    Optional<Budget> findByUserAndCategoryAndMonthAndYear(User user, String category, Integer month, Integer year);

    List<Budget> findByUserAndMonthAndYear(User user, Integer month, Integer year);
}