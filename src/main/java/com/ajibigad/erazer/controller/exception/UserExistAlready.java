package com.ajibigad.erazer.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ajibigad on 13/08/2017.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="User exist already")
public class UserExistAlready extends ErazerException{
    public UserExistAlready(String message) {
        super(message);
    }
}
