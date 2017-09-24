package com.ajibigad.erazer.controller;

import com.ajibigad.erazer.controller.exception.ResourceNotFound;
import com.ajibigad.erazer.controller.exception.UserExistAlready;
import com.ajibigad.erazer.event.ErazerEventPublisher;
import com.ajibigad.erazer.model.FcmToken;
import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.repository.FcmTokenRepository;
import com.ajibigad.erazer.repository.expense.ExpenseRepository;
import com.ajibigad.erazer.repository.expense.ExpenseSpecificationsBuilder;
import com.ajibigad.erazer.security.model.Role;
import com.ajibigad.erazer.security.model.User;
import com.ajibigad.erazer.security.repository.RoleRepository;
import com.ajibigad.erazer.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ajibigad on 13/08/2017.
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    FcmTokenRepository fcmTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ErazerEventPublisher eventPublisher;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws Exception {
        if (userRepository.exists(user.getUsername()) || userRepository.findByEmail(user.getEmail()) != null){
            throw new UserExistAlready("User exist");
        }
        List<Role> roles = new ArrayList<>();
        for (Role userRole : user.getRoles()){
            Role role = roleRepository.findByName(userRole.getName());
            if(role != null){
                roles.add(role);
            } else throw new ResourceNotFound(String.format("Role not found %s", userRole.getName()));
        }
        user.setRoles(new HashSet<>(roles));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @GetMapping("{username}")
    @PreAuthorize("(#username == principal.username) || hasRole('ADMIN')")
    public User getUser(@PathVariable String username){
        return userRepository.findByUsername(username);
    }

    @GetMapping
    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("{username}/expenses")
    @PreAuthorize(value = "hasRole('ADMIN') || (#username == principal.username)")
    public Iterable<Expense> getUserExpenses(@PathVariable String username,
                                             @RequestParam(required = false) String search){
        if(search != null){
            ExpenseSpecificationsBuilder builder = new ExpenseSpecificationsBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)=(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                if (matcher.group(1).equals("username")) continue;
                builder.with(matcher.group(1), matcher.group(2));
            }
            builder.with("username", username);

            Specification<Expense> spec = builder.build();
            return expenseRepository.findAll(spec);
        } else return expenseRepository.findAll();
    }

    @PostMapping(value = "fcm", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void addFcmToken(@RequestBody String fcmToken, Principal principal){
        User user = userRepository.findOne(principal.getName());
        if(fcmTokenRepository.exists(fcmToken)){
            FcmToken oldToken = fcmTokenRepository.findOne(fcmToken);
            fcmTokenRepository.delete(oldToken);
        }
        FcmToken newFcmToken = new FcmToken();
        newFcmToken.setUser(user);
        newFcmToken.setToken(fcmToken);
        fcmTokenRepository.save(newFcmToken);
        eventPublisher.publishFcmTokenAddedEvent(newFcmToken);
    }

    @DeleteMapping("fcm")
//    @PreAuthorize("(#token.user.username == principal.username)")
    public void deleteFcmToken(@RequestBody String fcmToken, Principal principal){
        FcmToken tokenToBeDeleted;
        if(fcmTokenRepository.exists(fcmToken)){
            tokenToBeDeleted = fcmTokenRepository.findOne(fcmToken);
            if(!tokenToBeDeleted.getUser().getUsername().equals(principal.getName())){
                return;
            }
            fcmTokenRepository.delete(fcmToken);
            eventPublisher.publishFcmTokenDeletedEvent(tokenToBeDeleted);
        }
    }

}
