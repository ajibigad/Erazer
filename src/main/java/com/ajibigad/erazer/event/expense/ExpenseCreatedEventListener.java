package com.ajibigad.erazer.event.expense;

import com.ajibigad.erazer.event.ErazerEventPublisher;
import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.security.model.Role;
import com.ajibigad.erazer.security.repository.RoleRepository;
import com.ajibigad.erazer.security.repository.UserRepository;
import com.ajibigad.erazer.service.fcm.FCMSendRequest;
import com.ajibigad.erazer.service.fcm.FCMService;
import com.ajibigad.erazer.service.fcm.Notification;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ajibigad on 15/08/2017.
 */
@Component
public class ExpenseCreatedEventListener implements ApplicationListener<ExpenseCreatedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ExpenseCreatedEventListener.class);

    @Autowired
    FCMService fcmService;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ExpenseCreatedEvent expenseCreatedEvent) {
        handleExpenseCreatedEvent(expenseCreatedEvent.getExpense());
    }

    private void handleExpenseCreatedEvent(Expense expense){

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            logger.info("No admin user found");
            return;
        }

        role.getUsers().forEach(user -> {
            if (!StringUtils.hasLength(user.getFcmNotificationKey())) {
                logger.info("admin user {} does not have notification key", user.getUsername());
                return;
            }
            Notification<Expense> notification = new Notification<>();
            notification.setTitle("new_expense_notification_title");
            notification.setTitleArgs(Collections.singletonList(expense.getUser().getUsername()));
            notification.setBody("new_expense_notification_body");
            notification.setBodyArgs(Arrays.asList(expense.getTitle(), String.format("%.2f", expense.getCost())));
            notification.setClickAction("com.ajibigad.erazer.EXPENSE_DETAILS_ACTION");

            FCMSendRequest<Expense> request = new FCMSendRequest<>();
            request.setTo(user.getFcmNotificationKey());
            request.setNotification(notification);
            request.setData(expense);

            fcmService.sendNotificationToUserDevices(request);
        });
    }
}
