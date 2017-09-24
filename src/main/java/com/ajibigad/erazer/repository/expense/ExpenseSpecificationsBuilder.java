package com.ajibigad.erazer.repository.expense;

import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.repository.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajibigad on 05/08/2017.
 */
public class ExpenseSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public ExpenseSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public ExpenseSpecificationsBuilder with(String key, Object value) {
        params.add(new SearchCriteria(key, value));
        return this;
    }

    public Specification<Expense> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Expense>> specs = new ArrayList<Specification<Expense>>();
        for (SearchCriteria param : params) {
            specs.add(new ExpenseSpecification(param));
        }

        Specification<Expense> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
