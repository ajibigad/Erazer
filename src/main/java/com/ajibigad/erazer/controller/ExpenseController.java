package com.ajibigad.erazer.controller;

import com.ajibigad.erazer.controller.exception.ErazerException;
import com.ajibigad.erazer.controller.exception.ResourceNotFound;
import com.ajibigad.erazer.event.ErazerEventPublisher;
import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.model.expense.PROOF_TYPE;
import com.ajibigad.erazer.model.expense.STATE;
import com.ajibigad.erazer.model.response.ExpensesOverview;
import com.ajibigad.erazer.repository.expense.ExpenseRepository;
import com.ajibigad.erazer.repository.expense.ExpenseSpecificationsBuilder;
import com.ajibigad.erazer.security.repository.UserRepository;
import com.ajibigad.erazer.utils.ImageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ajibigad on 05/08/2017.
 */
@RestController
@RequestMapping("expense")
public class ExpenseController {

    @Autowired
    ExpenseRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ErazerEventPublisher eventPublisher;

    @PostMapping
    public Expense createExpense(@RequestBody Expense expense, Principal principal) throws ErazerException {
        expense.setUser(userRepository.findByUsername(principal.getName()));
        if(expense.getProofType().equals(PROOF_TYPE.IMAGE)){
            expense.setProof(ImageHelper.saveImage(expense.getProof(), expense.getUser().getUsername()));
        }
        expense = repository.save(expense);
        eventPublisher.publishExpenseCreatedEvent(expense);
        return expense;
    }

    @GetMapping("{id}")
    public Expense getExpense(@PathVariable Long id){
        Expense expense = repository.findOne(id);
        if(expense == null) throw new ResourceNotFound("Expense not found");
        return expense;
    }

    @PatchMapping("{id}/changeState")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public Expense updateExpenseState(@PathVariable Long id, @RequestParam String state){
        Expense expense;
        if(repository.exists(id)){
            expense = repository.findOne(id);
            expense.setState(STATE.valueOf(state));
        } else throw new DataRetrievalFailureException("Expense not found");
        return repository.save(expense);
    }

    @RequestMapping("/images/{username}/{imageName}")
    @PreAuthorize("(#username == principal.username) || hasRole('ADMIN')")
    public HttpEntity<byte[]> getProfilePicture (@PathVariable String username, @PathVariable String imageName) throws ErazerException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        try {
            return new ResponseEntity<byte[]>(ImageHelper.getImage(username, imageName), httpHeaders, HttpStatus.OK);
        } catch (NoSuchFileException e) {
            e.printStackTrace();
            throw new ResourceNotFound("Image not found");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ErazerException(String.format("Failed to fetch %s's image %s", username, imageName));
        }
    }

    @GetMapping("all")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public Iterable<Expense> getAllExpenses(@RequestParam(required = false) String search){
        if(search != null){
            ExpenseSpecificationsBuilder builder = new ExpenseSpecificationsBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)=(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2));
            }

            Specification<Expense> spec = builder.build();
            return repository.findAll(spec);
        } else return repository.findAll();
    }

    @GetMapping("overview/all")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public List<ExpensesOverview> getAllExpensesOverview(@RequestParam(required = false) Integer month,
                                                         @RequestParam(required = false) Integer year){
        if((month != null && month > 0 && year !=null && year > 0)){
            return repository.findExpensesOveriew(month, year);
        } else return repository.findExpensesOveriew();
    }

    @GetMapping("overview/{username}")
    @PreAuthorize(value = "hasRole('ADMIN') || (#username == principal.username)")
    public List<ExpensesOverview> getUserExpensesOverview(@PathVariable String username,
                                                          @RequestParam(required = false) Integer month,
                                                         @RequestParam(required = false) Integer year){
        if((month != null && month > 0 && year !=null && year > 0)){
            return repository.findExpensesOveriewByUsername(username, month, year);
        } else return repository.findExpensesOveriewByUsername(username);
    }
}
