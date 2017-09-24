package com.ajibigad.erazer.event.fcm;

import com.ajibigad.erazer.model.FcmToken;
import com.ajibigad.erazer.security.model.User;
import com.ajibigad.erazer.security.repository.UserRepository;
import com.ajibigad.erazer.service.fcm.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Created by ajibigad on 16/08/2017.
 */
@Component
public class FcmTokenAddedEventListener implements ApplicationListener<FcmTokenAddedEvent> {

    @Autowired
    FCMService fcmService;

    @Autowired
    UserRepository userRepository;

    @Override
    public void onApplicationEvent(FcmTokenAddedEvent fcmTokenAddedEvent) {
        handleEvent(fcmTokenAddedEvent.getFcmToken());
    }

    private void handleEvent(FcmToken fcmToken){
        if(fcmToken.getUser().getFcmNotificationKey()==null ||
                fcmToken.getUser().getFcmNotificationKey().isEmpty()){
            String notificationKey = fcmService.createUserDeviceGroup(fcmToken);
            User user = fcmToken.getUser();
            user.setFcmNotificationKey(notificationKey);
            fcmToken.setUser(userRepository.save(user));
        }
        fcmService.addToUserDeviceGroup(fcmToken);
    }
}
