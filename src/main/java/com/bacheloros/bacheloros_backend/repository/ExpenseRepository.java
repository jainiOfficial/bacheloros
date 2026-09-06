package com.bacheloros.bacheloros_backend.repository;

import com.bacheloros.bacheloros_backend.entity.Expense;
import com.bacheloros.bacheloros_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByUserAndCategory(User user,String category);
    List<Expense> findByUser(User user);
    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    //@Query("...") — yeh humein khud JPQL (Java Persistence Query Language) likhne deta hai. JPQL raw SQL jaisa lagta hai, lekin table/column names ke bajaye Entity/field names use karta hai (Expense e, e.amount, e.user — jaisa tumne Java class mein likha, waisa hi).
    //SUM(e.amount) — yeh actual aggregation hai — saare matching rows ke amount field ka total.
    //COALESCE(SUM(e.amount), 0) — naya, important concept: agar koi expense match hi nahi karta (e.g. abhi tak "Food" category mein kuch kharch hi nahi hua), toh SUM() NULL return karta hai (0 nahi!). COALESCE(x, 0) ka matlab hai "agar x null hai toh 0 use karo." Isse NullPointerException bachega jab tum baad mein spentAmount ke saath arithmetic karoge (limitAmount.subtract(spentAmount) jaisa).
    //FUNCTION('MONTH', e.date) aur FUNCTION('YEAR', e.date) — e.date ek LocalDate hai (poori date — jaise 2026-08-15), lekin humein sirf month aur year nikalna hai usme se compare karne ke liye. FUNCTION('MONTH', ...) underlying database ke MONTH() SQL function ko call karta hai (Postgres mein bhi yeh kaam karta hai) — date se sirf month-number nikal leta hai.
    //:user, :category, :month, :year — yeh named parameters hain. @Param("user") unhe method ke actual arguments se bind karta hai.
//    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
//            "WHERE e.user = :user AND e.category = :category " +
//            "AND FUNCTION('MONTH', e.date) = :month AND FUNCTION('YEAR', e.date) = :year")
//    BigDecimal getTotalSpentByCategoryAndMonth(
//            @Param("user") User user,
//            @Param("category") String category,
//            @Param("month") Integer month,
//            @Param("year") Integer year
//    );

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.user = :user AND e.category = :category " +
            "AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSpentByCategoryAndDateRange(
            @Param("user") User user,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    // query to calculate the SUM of all expense made by particuler user in a month
    //start date or end date month ka starting and ending date h jo ki hmm noonth or year ki value se nikal rahe
    //jaha se ye function call ho raha udher dekho
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.user = :user AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal getTotalSpentByDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<Expense> findByUserAndCategoryAndDateBetweenOrderByDateDesc(
            User user, String category, LocalDate startDate, LocalDate endDate);
}
