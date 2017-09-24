package com.ajibigad.erazer.repository.expense;

import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.model.expense.PROOF_TYPE;
import com.ajibigad.erazer.model.expense.STATE;
import com.ajibigad.erazer.repository.SearchCriteria;
import com.ajibigad.erazer.security.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

/**
 * Created by ajibigad on 05/08/2017.
 */
public class ExpenseSpecification implements Specification<Expense> {

    SearchCriteria criteria;

    public ExpenseSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Expense> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        //replace user with username
        if(criteria.getKey().equals("username")){
            Join<Expense, User> expenseUserJoin = root.join("user");
            return criteriaBuilder.equal(expenseUserJoin.get("username"), criteria.getValue());
        } else if (criteria.getKey().equals("user")) return null;
        try {
            if (root.get(criteria.getKey()).getJavaType().isEnum()){
                Object value;
                if (root.get(criteria.getKey()).getJavaType() == STATE.class){
                    value = Enum.valueOf(STATE.class, criteria.getValue().toString());
                } else {
                    value = Enum.valueOf(PROOF_TYPE.class, criteria.getValue().toString());
                }
                return criteriaBuilder.equal(root.get(criteria.getKey()), value);
            }
            return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
        } catch (IllegalArgumentException ex){
            //to ignore keys that don't exist in Expense model
            return null;
        }

    }
}
