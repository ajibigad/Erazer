package com.ajibigad.erazer.event.fcm;

import com.ajibigad.erazer.model.FcmToken;
import org.springframework.context.ApplicationEvent;

/**
 * Created by ajibigad on 24/09/2017.
 */
public class FcmTokenDeletedEvent extends FcmTokenEvent {

    public FcmTokenDeletedEvent(Object source, FcmToken fcmToken) {
        super(source, fcmToken);
    }
}
