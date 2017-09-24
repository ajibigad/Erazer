package com.ajibigad.erazer.service.fcm;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Created by ajibigad on 16/08/2017.
 * T is data type for data
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification<T> {

    @JsonProperty("title_loc_key")
    private String title;

    @JsonProperty("title_loc_args")
    private List<String> titleArgs;

    @JsonProperty("body_loc_key")
    private String body;

    @JsonProperty("body_loc_args")
    private List<String> bodyArgs;

    @JsonProperty("click_action")
    private String clickAction;

    private String sound = "default";

    private String icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTitleArgs() {
        return titleArgs;
    }

    public void setTitleArgs(List<String> titleArgs) {
        this.titleArgs = titleArgs;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getBodyArgs() {
        return bodyArgs;
    }

    public void setBodyArgs(List<String> bodyArgs) {
        this.bodyArgs = bodyArgs;
    }

    public String getClickAction() {
        return clickAction;
    }

    public void setClickAction(String clickAction) {
        this.clickAction = clickAction;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public static class NotificationRequest{

        private String operation;

        @JsonProperty("notification_key_name")
        private String notificationKeyName;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("notification_key")
        private String getNotificationKey;

        @JsonProperty("registration_ids")
        private List<String> registrationIds;

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getNotificationKeyName() {
            return notificationKeyName;
        }

        public void setNotificationKeyName(String notificationKeyName) {
            this.notificationKeyName = notificationKeyName;
        }

        public String getGetNotificationKey() {
            return getNotificationKey;
        }

        public void setGetNotificationKey(String getNotificationKey) {
            this.getNotificationKey = getNotificationKey;
        }

        public List<String> getRegistrationIds() {
            return registrationIds;
        }

        public void setRegistrationIds(List<String> registrationIds) {
            this.registrationIds = registrationIds;
        }
    }

    public class NotificationResponse{

    }
}
