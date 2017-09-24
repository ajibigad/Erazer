package com.ajibigad.erazer.repository.expense;

import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.model.response.ExpensesOverview;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by ajibigad on 05/08/2017.
 */

public interface ExpenseRepository  extends PagingAndSortingRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    @Query("select new com.ajibigad.erazer.model.response.ExpensesOverview(e.state, count(e)) from Expense e group by e.state")
    List<ExpensesOverview> findExpensesOveriew();

    @Query("select new com.ajibigad.erazer.model.response.ExpensesOverview(e.state, count(e)) " +
            "from Expense e where e.user.username = ?1 group by e.state")
    List<ExpensesOverview> findExpensesOveriewByUsername(String username);

//    @Query("select new com.ajibigad.erazer.model.response.ExpensesOverview(e.state, count(e)) from Expense e" +
//            "where new LocalDate(e.dateAdded) = :month and year(e.dateAdded) = year group by e.state")
    @Query("select new com.ajibigad.erazer.model.response.ExpensesOverview(e.state, count(e)) from Expense e group by e.state")
    List<ExpensesOverview> findExpensesOveriew(int month, int year);

    @Query("select new com.ajibigad.erazer.model.response.ExpensesOverview(e.state, count(e)) " +
            "from Expense e where e.user.username = ?1 group by e.state")
    List<ExpensesOverview> findExpensesOveriewByUsername(String username, int month, int year);
}
