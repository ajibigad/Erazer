package com.ajibigad.erazer.model.response;

import com.ajibigad.erazer.model.expense.STATE;

/**
 * Created by ajibigad on 05/08/2017.
 */
public class ExpensesOverview {
    private Long count;
    private STATE state;

    public ExpensesOverview(STATE state, Long count) {
        this.count = count;
        this.state = state;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }
}
