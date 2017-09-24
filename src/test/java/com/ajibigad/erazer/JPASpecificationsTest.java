package com.ajibigad.erazer;

import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.model.expense.PROOF_TYPE;
import com.ajibigad.erazer.model.expense.STATE;
import com.ajibigad.erazer.repository.expense.ExpenseRepository;
import com.ajibigad.erazer.repository.expense.ExpenseSpecification;
import com.ajibigad.erazer.repository.expense.ExpenseSpecificationsBuilder;
import com.ajibigad.erazer.repository.SearchCriteria;
import com.ajibigad.erazer.security.model.Role;
import com.ajibigad.erazer.security.model.User;
import com.ajibigad.erazer.security.repository.RoleRepository;
import com.ajibigad.erazer.security.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.assertThat;

/**
 * Created by ajibigad on 05/08/2017.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@DataJpaTest
@ComponentScan
public class JPASpecificationsTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ExpenseRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    private Expense expense;

    @Before
    public void init() {
        
        User testuser = new com.ajibigad.erazer.security.model.User();
        testuser.setUsername("testuser");
        testuser.setEmail("testuser@gmail.com");
        testuser.setPassword(passwordEncoder.encode("testuserpass"));
        testuser.setFirstname("Superuser");
        testuser.setLastname("testuser");
        testuser.setRoles(new HashSet<Role>(Arrays.asList(roleRepository.findByName("ROLE_ADMIN"))));
        userRepository.save(testuser);

        expense = new Expense();
        expense.setTitle("new laptop charger");
        expense.setCost(135);
        expense.setDescription("Bought a new laptop charger");
        expense.setUser(testuser);
        expense.setProofType(PROOF_TYPE.IMAGE);
        expense.setProof("nonvoern");
        expense.setState(STATE.APPROVED);
        repository.save(expense);
    }

    @Test
    public void testMultipleSearchCriteria() {
        ExpenseSpecification spec =
                new ExpenseSpecification(new SearchCriteria("state", STATE.APPROVED));

        List<Expense> results = repository.findAll(new ExpenseSpecificationsBuilder().with("cost", 135)
                .with("state", STATE.APPROVED).build());

        List<Expense> results2 = repository.findAll(new ExpenseSpecificationsBuilder().with("user", "testuser").build());

        assertThat(expense, isIn(results));
        assertThat(results2, hasSize(1));
    }
}
