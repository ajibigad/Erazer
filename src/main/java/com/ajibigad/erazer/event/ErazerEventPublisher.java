package com.ajibigad.erazer.event;

import com.ajibigad.erazer.event.expense.ExpenseCreatedEvent;
import com.ajibigad.erazer.event.fcm.FcmTokenAddedEvent;
import com.ajibigad.erazer.event.fcm.FcmTokenDeletedEvent;
import com.ajibigad.erazer.model.FcmToken;
import com.ajibigad.erazer.model.expense.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by ajibigad on 15/08/2017.
 */

@Component
public class ErazerEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ErazerEventPublisher.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishExpenseCreatedEvent(final Expense expense) {
        ExpenseCreatedEvent expenseCreatedEvent = new ExpenseCreatedEvent(this, expense);
        applicationEventPublisher.publishEvent(expenseCreatedEvent);
        logger.info("Published expense created event");
    }

    public void publishFcmTokenAddedEvent(final FcmToken fcmToken){
        FcmTokenAddedEvent fcmTokenAddedEvent = new FcmTokenAddedEvent(this, fcmToken);
        applicationEventPublisher.publishEvent(fcmTokenAddedEvent);
        logger.info("Published fcm token added event");
    }

    public void publishFcmTokenDeletedEvent(final FcmToken fcmToken){
        FcmTokenDeletedEvent fcmTokenDeletedEvent = new FcmTokenDeletedEvent(this, fcmToken);
        applicationEventPublisher.publishEvent(fcmTokenDeletedEvent);
        logger.info("Published fcm token deleted event");
    }
}
