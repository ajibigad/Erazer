package com.ajibigad.erazer.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ajibigad on 13/08/2017.
 */
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
public class ErazerException extends RuntimeException {

    public ErazerException(String message) {
        super(message);
    }
}
