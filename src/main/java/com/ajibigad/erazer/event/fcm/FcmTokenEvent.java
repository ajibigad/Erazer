package com.ajibigad.erazer.event.fcm;

import com.ajibigad.erazer.model.FcmToken;
import org.springframework.context.ApplicationEvent;

/**
 * Created by ajibigad on 24/09/2017.
 */
public abstract class FcmTokenEvent extends ApplicationEvent {
    private FcmToken fcmToken;

    public FcmTokenEvent(Object source, FcmToken fcmToken) {
        super(source);
        this.fcmToken = fcmToken;
    }

    public FcmToken getFcmToken() {
        return fcmToken;
    }
}
