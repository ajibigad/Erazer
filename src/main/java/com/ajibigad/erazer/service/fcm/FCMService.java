package com.ajibigad.erazer.service.fcm;

import com.ajibigad.erazer.model.FcmToken;
import com.ajibigad.erazer.model.expense.Expense;
import com.ajibigad.erazer.security.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by ajibigad on 15/08/2017.
 */
@Service
public class FCMService {

    private static final String FCM_SEND_API = "https://fcm.googleapis.com/fcm/send";
    private static final String FCM_NOTIFICATION_API = "https://android.googleapis.com/gcm/notification";

    @Value("${fcm.server.key}")
    private String SERVER_KEY;

    @Value("${fcm.sender.id}")
    private String SENDER_ID;

    private static Logger logger = LoggerFactory.getLogger(FCMService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.interceptors((ClientHttpRequestInterceptor) (httpRequest, bytes, clientHttpRequestExecution) -> {
            httpRequest.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            httpRequest.getHeaders().put("Authorization", Collections.singletonList("key="+SERVER_KEY));
            logger.info("===========================request begin================================================");
            logger.info("URI         : {}", httpRequest.getURI());
            logger.info("Method      : {}", httpRequest.getMethod());
            logger.info("Headers     : {}", httpRequest.getHeaders() );
            logger.info("Request body: {}", new String(bytes, "UTF-8"));
            logger.info("==========================request end================================================");

            ClientHttpResponse response = null;
            BufferedReader bufferedReader = null;
            StringBuilder inputStringBuilder = new StringBuilder();
            try {
                response =  clientHttpRequestExecution.execute(httpRequest, bytes);
                bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
                String line = bufferedReader.readLine();
                while (line != null) {
                    inputStringBuilder.append(line);
                    inputStringBuilder.append('\n');
                    line = bufferedReader.readLine();
                }
                logger.info("============================response begin==========================================");
                logger.info("Status code  : {}", response.getStatusCode());
                logger.info("Status text  : {}", response.getStatusText());
                logger.info("Headers      : {}", response.getHeaders());
                logger.info("Response body: {}", inputStringBuilder.toString());
                logger.info("=======================response end=================================================");
            } catch (IOException ex){

            }
            return response;
        }).requestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory())).build();
    }

    public void sendNotificationToUserDevices(FCMSendRequest request){
        ResponseEntity<FCMResponse> responseEntity = restTemplate.postForEntity(FCM_SEND_API, request, FCMResponse.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
//            logger.info("", responseEntity.getBody().);
        }

        //if successful then check the response to know the status of unsuccessful tokens and remove such tokens
        //else save the notification and user to be retried later;
    }

    public String createUserDeviceGroup(FcmToken fcmToken){
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        requestHeaders.put("project_id", Collections.singletonList(SENDER_ID));

        Notification.NotificationRequest requestBody = new Notification.NotificationRequest();
        requestBody.setOperation("create");
        requestBody.setNotificationKeyName(fcmToken.getUser().getUsername());
        requestBody.setRegistrationIds(Collections.singletonList(fcmToken.getToken()));

        HttpEntity<Notification.NotificationRequest> request = new HttpEntity<>(requestBody, requestHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(FCM_NOTIFICATION_API, request, Map.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            logger.info("Notification token for {}, {}", fcmToken.getUser().getUsername(), responseEntity.getBody().get("notification_key"));
            return (String) responseEntity.getBody().get("notification_key");
        } else return "";
    }

    public boolean addToUserDeviceGroup(FcmToken fcmToken){
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        requestHeaders.put("project_id", Collections.singletonList(SENDER_ID));

        Notification.NotificationRequest requestBody = new Notification.NotificationRequest();
        requestBody.setOperation("add");
        requestBody.setNotificationKeyName(fcmToken.getUser().getUsername());
        requestBody.setGetNotificationKey(fcmToken.getUser().getFcmNotificationKey());
        requestBody.setRegistrationIds(Collections.singletonList(fcmToken.getToken()));

        HttpEntity<Notification.NotificationRequest> request = new HttpEntity<>(requestBody, requestHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(FCM_NOTIFICATION_API, request, Map.class);
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    public boolean deleteFromUserDeviceGroup(FcmToken fcmToken){
        MultiValueMap<String, String> requestHeaders = new LinkedMultiValueMap<>();
        requestHeaders.put("project_id", Collections.singletonList(SENDER_ID));

        Notification.NotificationRequest requestBody = new Notification.NotificationRequest();
        requestBody.setOperation("remove");
        requestBody.setNotificationKeyName(fcmToken.getUser().getUsername());
        requestBody.setGetNotificationKey(fcmToken.getUser().getFcmNotificationKey());
        requestBody.setRegistrationIds(Collections.singletonList(fcmToken.getToken()));

        HttpEntity<Notification.NotificationRequest> request = new HttpEntity<>(requestBody, requestHeaders);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(FCM_NOTIFICATION_API, request, Map.class);
        return responseEntity.getStatusCode().is2xxSuccessful();
    }
}
