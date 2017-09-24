package com.ajibigad.erazer.event.fcm;

import com.ajibigad.erazer.model.FcmToken;
import org.springframework.context.ApplicationEvent;

/**
 * Created by ajibigad on 16/08/2017.
 */
public class FcmTokenAddedEvent extends FcmTokenEvent {
    public FcmTokenAddedEvent(Object source, FcmToken fcmToken) {
        super(source, fcmToken);
    }
}
