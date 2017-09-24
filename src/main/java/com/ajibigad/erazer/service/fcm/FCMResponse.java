package com.ajibigad.erazer.service.fcm;

/**
 * Created by ajibigad on 16/08/2017.
 */
public class FCMResponse {

    private String success;
    private String failure;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }
}
