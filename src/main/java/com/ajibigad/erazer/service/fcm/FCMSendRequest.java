package com.ajibigad.erazer.service.fcm;

/**
 * Created by ajibigad on 16/08/2017.
 */
public class FCMSendRequest<T> {

    private String to;
    private Notification notification;

    private T data;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
