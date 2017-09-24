package com.ajibigad.erazer.event.fcm;

import com.ajibigad.erazer.model.FcmToken;
import com.ajibigad.erazer.service.fcm.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by ajibigad on 24/09/2017.
 */
@Component
public class FcmTokenDeletedEventListener implements ApplicationListener<FcmTokenDeletedEvent> {

    @Autowired
    FCMService fcmService;

    @Override
    public void onApplicationEvent(FcmTokenDeletedEvent fcmTokenDeletedEvent) {
        handleEvent(fcmTokenDeletedEvent.getFcmToken());
    }

    private void handleEvent(FcmToken fcmToken) {
        fcmService.deleteFromUserDeviceGroup(fcmToken);
    }
}
