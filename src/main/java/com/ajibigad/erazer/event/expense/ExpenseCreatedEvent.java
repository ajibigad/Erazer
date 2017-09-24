package com.ajibigad.erazer.event.expense;

import com.ajibigad.erazer.model.expense.Expense;
import org.springframework.context.ApplicationEvent;

/**
 * Created by ajibigad on 15/08/2017.
 */
public class ExpenseCreatedEvent extends ApplicationEvent{

    private Expense expense;

    public ExpenseCreatedEvent(Object source, Expense expense) {
        super(source);
        this.expense = expense;
    }

    public Expense getExpense() {
        return expense;
    }
}
